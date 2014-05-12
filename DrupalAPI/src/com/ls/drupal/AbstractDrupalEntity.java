package com.ls.drupal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.eclipse.jdt.annotation.NonNull;

import com.android.volley.VolleyError;
import com.google.gson.annotations.Expose;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.http.base.ICharsetItem;
import com.ls.http.base.ResponseData;
import com.ls.util.ObjectComparator;
import com.ls.util.ObjectComparator.FootPrint;

public abstract class AbstractDrupalEntity implements DrupalClient.OnResponseListener, ICharsetItem
{
	transient private DrupalClient drupalClient; // Prevent entity from being
													// posted to
													// server or fetched from
													// there.
	transient private OnEntityRequestListener requestListener;

	transient private RequestProgressListener progressListener;

	transient private FootPrint footprint;

	transient private final AtomicInteger activeRequestCount;

	/**
	 * @return path to resource. Shouldn't include base URL(domain).
	 */
	protected abstract String getPath();

	/**
	 * Called for post requests only
	 * 
	 * @return post parameters to be published on server or null if default
	 *         object serialization has to be performed.
	 */
	protected abstract Map<String, String> getItemRequestPostParameters();

	protected abstract Map<String, String> getItemRequestGetParameters(RequestMethod method);

	abstract @NonNull
	Object getManagedData();

	/**
	 * Method can be overriden in order to specify non-default charset for
	 * Entity.
	 */
	@Override
	public String getCharset()
	{
		return null;
	}

	public AbstractDrupalEntity(DrupalClient client)
	{
		this.drupalClient = client;
		this.activeRequestCount = new AtomicInteger(0);
	}

	/**
	 * In case of request canceling - no method will be triggered.
	 * 
	 * @author lemberg
	 * 
	 */
	public interface OnEntityRequestListener
	{
		void onEntityFetched(AbstractDrupalEntity entity);

		void onEntityPosted(AbstractDrupalEntity entity);

		void onEntityPatched(AbstractDrupalEntity entity);

		void onEntityRemoved(AbstractDrupalEntity entity);

		void onRequestFailed(VolleyError error, AbstractDrupalEntity entity);
	}

	/**
	 * Can be used in order to react on request count changes
	 * (start/completition/failure or canceling).
	 * 
	 * @author lemberg
	 * 
	 */
	public interface RequestProgressListener
	{
		void onRequestStarted(AbstractDrupalEntity theEntity, int activeRequests);

		void onRequestFinished(AbstractDrupalEntity theEntity, int activeRequests);
	}

	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass
	 *            class of result or null if no result needed.
	 * @return @class ResponseData entity, containing server response string and
	 *         code or error in case of synchronous request, null otherwise
	 */
	public ResponseData postDataToServer(boolean synchronous, Class<?> resultClass)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		ResponseData result = this.drupalClient.postObject(this, resultClass, RequestMethod.POST, this, synchronous);
		this.onNewRequestStarted();
		return result;
	}

	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @return @class ResponseData entity, containing server response or error
	 *         in case of synchronous request, null otherwise
	 */
	public ResponseData getDataFromServer(boolean synchronous)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		ResponseData result = this.drupalClient.getObject(this, this.getManagedDataClassSpecifyer(), RequestMethod.GET, this, synchronous);
		;
		this.onNewRequestStarted();
		return result;
	}

	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass
	 *            class of result or null if no result needed.
	 * @return @class ResponseData entity, containing server response or error
	 *         in case of synchronous request, null otherwise
	 */
	public ResponseData deleteDataFromServer(boolean synchronous, Class<?> resultClass)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		ResponseData result = this.drupalClient.deleteObject(this, resultClass, RequestMethod.DELETE, this, synchronous);
		this.onNewRequestStarted();
		return result;
	}

	// OnResponceListener methods
	public void onResponceReceived(ResponseData data, Object tag)
	{
		this.onNewRequestComplete();
		RequestMethod method = (RequestMethod) tag;

		if (method == RequestMethod.GET)
		{
			this.consumeObject(data.getData());
		}

		if (this.requestListener != null)
		{
			switch (method) {
			case POST:
				this.requestListener.onEntityPosted(this);
				break;
			case DELETE:
				this.requestListener.onEntityRemoved(this);
				break;
			case PATCH:
				this.requestListener.onEntityPatched(this);
				break;
			case GET:
				this.requestListener.onEntityFetched(this);
				break;
			}
		}
	}

	/**
	 * Clone all fields of object specified to current one You can override this
	 * method in order to perform custom one
	 * 
	 * @param entity
	 *            object to be consumed
	 */
	protected void consumeObject(Object entity)
	{
		Object consumer = this.getManagedDataChecked();
		Class<?> currentClass = consumer.getClass();
		while (!Object.class.equals(currentClass))
		{
			Field[] fields = currentClass.getDeclaredFields();
			for (int counter = 0; counter < fields.length; counter++)
			{
				Field field = fields[counter];
				Expose expose = field.getAnnotation(Expose.class);
				if (expose != null && !expose.deserialize() || Modifier.isTransient(field.getModifiers()))
				{
					continue;// We don't have to copy ignored fields.
				}
				field.setAccessible(true);
				Object value;
				try
				{
					value = field.get(entity);
					// if(value != null)
					// {
					field.set(consumer, value);
					// }
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
			}
			currentClass = currentClass.getSuperclass();
		}
	}

	/**
	 * You can override this method in order to Parse custom classes from "get"
	 * request response.
	 * 
	 * @return Class or type of managed object.
	 */
	protected Object getManagedDataClassSpecifyer()
	{
		return this.getManagedData().getClass();
	}

	public void onError(VolleyError error, Object tag)
	{
		if (this.requestListener != null)
		{
			this.requestListener.onRequestFailed(error, this);
		}
		this.onNewRequestComplete();
	}

	@Override
	public void onCancel(Object tag)
	{
		this.onNewRequestComplete();
	}

	public DrupalClient getDrupalClient()
	{
		return drupalClient;
	}

	public void setDrupalClient(DrupalClient drupalClient)
	{
		this.drupalClient = drupalClient;
	}

	public RequestProgressListener getProgressListener()
	{
		return progressListener;
	}

	public void setProgressListener(RequestProgressListener progressListener)
	{
		this.progressListener = progressListener;
	}

	public OnEntityRequestListener getRequestListener()
	{
		return requestListener;
	}

	public void setRequestListener(OnEntityRequestListener requestListener)
	{
		this.requestListener = requestListener;
	}

	// Request canceling
	public void cancellAllRequests()
	{
		this.drupalClient.cancelAllRequestsForListener(this, null);
	}

	// Patch method management

	/**
	 * Creates footprint to be used later in order to calculate differences for
	 * patch request.
	 */
	public void createFootPrint()
	{
		this.footprint = getCurrentStateFootprint();
	}

	protected FootPrint getCurrentStateFootprint()
	{
		ObjectComparator comparator = new ObjectComparator();
		return comparator.createFootPrint(this.getManagedDataChecked());
	}

	/**
	 * 
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass
	 *            class of result or null if no result needed.
	 * @return @class ResponseData entity, containing server response and
	 *         resultClass instance or error in case of synchronous request,
	 *         null otherwise
	 * @throws IllegalStateException
	 *             in case if there are no changes to post. You can check if
	 *             there are ones, calling <code>canPatch()</code> method.
	 */
	public ResponseData patchDataOnServer(boolean synchronous, Class<?> resultClass) throws IllegalStateException
	{
		ResponseData result = this.drupalClient.patchObject(this, resultClass, RequestMethod.PATCH, this, synchronous);
		this.onNewRequestStarted();
		return result;
	}

	public Object getPatchObject()
	{
		ObjectComparator comparator = new ObjectComparator();
		FootPrint currentState = comparator.createFootPrint(this.getManagedDataChecked());

		@SuppressWarnings("null")
		Object difference = this.getDifference(this.footprint, currentState, comparator);
		if (difference != null)
		{
			return difference;
		} else
		{
			throw new IllegalStateException("There are no changes to post, check canPatch() call before");
		}
	}

	/**
	 * @return true if there are changes to post, false otherwise
	 */
	@SuppressWarnings("null")
	public boolean canPatch()
	{
		ObjectComparator comparator = new ObjectComparator();
		return this.canPatch(this.footprint, comparator.createFootPrint(this), comparator);
	}

	private boolean canPatch(@NonNull FootPrint origin, @NonNull FootPrint current, @NonNull ObjectComparator comparator)
	{
		Object difference = this.getDifference(origin, current, comparator);
		return (difference != null);
	}

	private Object getDifference(@NonNull FootPrint origin, @NonNull FootPrint current, ObjectComparator comparator)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		Assert.assertNotNull("You have to make initial objects footprint in order to calculate changes", origin);
		return comparator.getDifferencesJSON(origin, current);
	}

	// Manage request progress

	public int getActiveRequestCount()
	{
		return activeRequestCount.get();
	}

	private void onNewRequestStarted()
	{
		if (this.progressListener != null)
		{
			synchronized (this.progressListener)
			{
				int requestCount = this.activeRequestCount.incrementAndGet();
				this.progressListener.onRequestStarted(this, requestCount);
			}
		} else
		{
			this.activeRequestCount.incrementAndGet();
		}
	}

	private void onNewRequestComplete()
	{
		if (this.progressListener != null)
		{
			synchronized (this.progressListener)
			{
				int requestCount = this.activeRequestCount.decrementAndGet();
				this.progressListener.onRequestFinished(this, requestCount);
			}
		} else
		{
			this.activeRequestCount.decrementAndGet();
		}
	}

	@NonNull
	private Object getManagedDataChecked()
	{
		Assert.assertNotNull("You have to specify non null data object", this.getManagedData());
		return getManagedData();
	}
}
