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
import com.ls.http.base.IPostableItem;
import com.ls.http.base.IResponceItem;
import com.ls.http.base.ResponseData;
import com.ls.util.ObjectComparator;
import com.ls.util.ObjectComparator.FootPrint;

public abstract class AbstractBaseDrupalEntity implements DrupalClient.OnResponseListener, ICharsetItem
{
	transient private DrupalClient drupalClient; 
	transient private OnEntityRequestListener requestListener;

	transient private RequestProgressListener progressListener;

	transient private FootPrint footprint;

	transient private final AtomicInteger activeRequestCount;

	/**
	 * In case of request canceling - no method will be triggered.
	 * 
	 * @author lemberg
	 * 
	 */
	public interface OnEntityRequestListener
	{
		void onEntityPulled(AbstractBaseDrupalEntity entity, ResponseData data);

		void onEntityPushed(AbstractBaseDrupalEntity entity, ResponseData data);

		void onEntityPatched(AbstractBaseDrupalEntity entity, ResponseData data);

		void onEntityRemoved(AbstractBaseDrupalEntity entity, ResponseData data);

		void onRequestFailed(AbstractBaseDrupalEntity entity, VolleyError error);
	}	
	
	/**
	 * Can be used in order to react on request count changes
	 * (start/success/failure or canceling).
	 * 
	 * @author lemberg
	 * 
	 */
	public interface RequestProgressListener
	{
		/**
		 * Called after new request was added to queue
		 * @param theEntity
		 * @param activeRequests number of requests pending
		 */
		void onRequestStarted(AbstractBaseDrupalEntity theEntity, int activeRequests);

		/**
		 * Called after current request was complete
		 * @param theEntity
		 * @param activeRequests number of requests pending
		 */
		void onRequestFinished(AbstractBaseDrupalEntity theEntity, int activeRequests);
	}

	
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

	/**	 
	 * @param method is instance od {@link RequestMethod} enum, this method is called for. it can be "GET", "POST", "PATCH" or "DELETE".
	 * @return parameters for the request method specified.
	 */
	protected abstract Map<String, String> getItemRequestGetParameters(RequestMethod method);

	/** Get data object, used to perform perform get/post/patch/delete requests.
	 * @return data object. Can implement {@link IPostableItem} or {@link IResponceItem} in order to handle json/xml serialization/deserialization manually.
	 */
	abstract @NonNull Object getManagedData();
	
	@Override
	public String getCharset()
	{
		return null;
	}

	public AbstractBaseDrupalEntity(DrupalClient client)
	{
		this.drupalClient = client;
		this.activeRequestCount = new AtomicInteger(0);
	}
	
	
	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass
	 *            class of result or null if no result needed.
	 * @return @class ResponseData entity, containing server response string and
	 *         code or error in case of synchronous request, null otherwise
	 */
	public ResponseData pushToServer(boolean synchronous, Class<?> resultClass)
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
	public ResponseData pullFromServer(boolean synchronous)
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
	public ResponseData deleteFromServer(boolean synchronous, Class<?> resultClass)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		ResponseData result = this.drupalClient.deleteObject(this, resultClass, RequestMethod.DELETE, this, synchronous);
		this.onNewRequestStarted();
		return result;
	}

	// OnResponceListener methods
	
	public final void onResponceReceived(ResponseData data, Object tag)
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
				this.requestListener.onEntityPushed(this,data);
				break;
			case DELETE:
				this.requestListener.onEntityRemoved(this,data);
				break;
			case PATCH:
				this.requestListener.onEntityPatched(this,data);
				break;
			case GET:
				this.requestListener.onEntityPulled(this,data);
				break;
			}
		}
	}
	
	public final void onError(VolleyError error, Object tag)
	{
		if (this.requestListener != null)
		{
			this.requestListener.onRequestFailed(this,error);
		}
		this.onNewRequestComplete();
	}

	@Override
	public final void onCancel(Object tag)
	{
		this.onNewRequestComplete();
	}

	/**
	 * Method is used in order to apply server response result object to current instance 
	 * and clone all fields of object specified to current one You can override this
	 * method in order to perform custom cloning.
	 *  
	 * @param entity
	 *            object to be consumed
	 */
	protected void consumeObject(Object entity)
	{
		Object consumer = this.getManagedDataChecked();
		AbstractBaseDrupalEntity.consumeObject(consumer, entity);
	}
	
	/**
	 * Utility method, used to clone all entities non-transient fields to the consumer
	 * @param consumer
	 * @param entity
	 */
	public static void consumeObject(Object consumer,Object entity)
	{
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
	 * You can override this method in order to gain custom classes from "GET"
	 * Note:  you will also have to override {@link AbstractBaseDrupalEntity#consumeObject(Object)} method in order to apply modified response class
	 * request response.
	 * 
	 * @return Class or type of managed object.
	 */
	protected Object getManagedDataClassSpecifyer()
	{
		return this.getManagedData().getClass();
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
	
	/**
	 * Release current footprint (is recommended to perform after successful patch request)
	 */
	public void clearFootPrint()
	{
		this.footprint = null;
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
	public ResponseData patchServerData(boolean synchronous, Class<?> resultClass) throws IllegalStateException
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

	/**	 
	 * @return number of requests pending
	 */
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
