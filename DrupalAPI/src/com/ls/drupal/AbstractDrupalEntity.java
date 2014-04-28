package com.ls.drupal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.jdt.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.gson.annotations.Expose;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.http.base.ICharsetItem;
import com.ls.http.base.ResponseData;
import com.ls.utiles.ObjectComparator;
import com.ls.utiles.ObjectComparator.FootPrint;

public abstract class AbstractDrupalEntity implements DrupalClient.OnResponseListener, ICharsetItem
{	
	transient private DrupalClient drupalClient; // Prevent entity from being posted to
												// server or fetched from there.	
	transient private OnEntityRequestListener listener;
		
	transient private FootPrint footprint;

	/**
	 * @return path to resource. Shouldn't include base URL(domain).
	 */
	protected abstract String getPath();

	/**
	 * Called for post requests only
	 * @return post parameters to be published on server or null if default object serialization has to be performed.
	 */
	protected abstract Map<String, String> getItemRequestPostParameters();

	protected abstract Map<String, String> getItemRequestGetParameters(RequestMethod method);

	public AbstractDrupalEntity(DrupalClient client)
	{
		this.drupalClient = client;				
		Assert.assertNotNull("You have to specify non null data object",this.getManagedData());
	}

	public interface OnEntityRequestListener
	{
		void onEntityFetched(AbstractDrupalEntity entity);

		void onEntityPosted(AbstractDrupalEntity entity);

		void onEntityPatched(AbstractDrupalEntity entity);

		void onEntityRemoved(AbstractDrupalEntity entity);

		void onRequestFailed(VolleyError error, AbstractDrupalEntity entity);
	}

	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass class of result or null if no result needed.
	 * @return @class ResponseData entity, containing server response string and code or error
	 *         in case of synchronous request, null otherwise
	 */
	public ResponseData postDataToServer(boolean synchronous, Class<?> resultClass)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		return this.drupalClient.postObject(this, resultClass, RequestMethod.POST, this, synchronous);
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
		return this.drupalClient.getObject(this, this.getManagedData().getClass(), RequestMethod.GET, this, synchronous);
	}
	
	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass class of result or null if no result needed.
	 * @return @class ResponseData entity, containing server response or error
	 *         in case of synchronous request, null otherwise
	 */
	public ResponseData deleteDataFromServer(boolean synchronous, Class<?> resultClass)
	{		
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);	
		return this.drupalClient.deleteObject(this, resultClass, RequestMethod.DELETE, this, synchronous);
	}
	
	
	// OnResponceListener methods
	public void onResponceReceived(ResponseData data, Object tag)
	{
		RequestMethod method = (RequestMethod)tag;
		
		if(method == RequestMethod.GET)
		{
			this.consumeObject((AbstractDrupalEntity)data.getData());
		}
		
		if (this.listener != null)
		{
			switch (method) 
			{
			case POST:
				this.listener.onEntityPosted(this);
				break;
			case DELETE:
				this.listener.onEntityRemoved(this);
				break;
			case PATCH:
				this.listener.onEntityPatched(this);
				break;
			case GET:			
				this.listener.onEntityFetched(this);
				break;
			}
		}
	}

	/**	Clone all fields of object specified to current one
	 * @param entity object to be consumed
	 */
	private void consumeObject(AbstractDrupalEntity entity)
	{
		Object consumer = this.getManagedData();
		Field[] fields = consumer.getClass().getFields();
		for(int counter = 0; counter < fields.length;counter++)
		{
			Field field = fields[counter];
			Expose expose = field.getAnnotation(Expose.class);			
			if(expose != null && !expose.deserialize() || Modifier.isTransient(field.getModifiers()))
			{
				continue;//We don't have to copy ignored fields.
			}
			field.setAccessible(true);
			Object value;
			try
			{
				value = field.get(entity);
//				if(value != null)
//				{
					field.set(consumer, value);
//				}
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}			
		}
	}
			
	
	public void onError(VolleyError error, Object tag)
	{
		if (this.listener != null)
		{
			this.listener.onRequestFailed(error, this);
		}
	}

	public DrupalClient getDrupalClient()
	{
		return drupalClient;
	}

	public void setDrupalClient(DrupalClient drupalClient)
	{
		this.drupalClient = drupalClient;
	}

	public OnEntityRequestListener getListener()
	{
		return listener;
	}

	public void setListener(OnEntityRequestListener listener)
	{
		this.listener = listener;
	}
	
	//Request canceling
	public void cancellAllRequests()
	{
		this.drupalClient.cancelAllRequestsForListener(this, null);
	}
	
	//Patch method management
	
	/**
	 * Creates footprint to be used later in order to calculate differences for patch request.
	 */
	public void createFootPrint()
	{		
		this.footprint = getCurrentStateFootprint();
	}
	
	protected FootPrint getCurrentStateFootprint()
	{
		ObjectComparator comparator = new ObjectComparator();
		return comparator.createFootPrint(this.getManagedData());
	}
			
	/**
	 * 
	 * @param synchronous if true - request will be performed synchronously.
	 * @param resultClass class of result or null if no result needed.	 	 
	 * @return @class ResponseData entity, containing server response and resultClass instance or error
	 *         in case of synchronous request, null otherwise
	 * @throws IllegalStateException in case if there are no changes to post. You can check if there are ones, calling <code>canPatch()</code> method.
	 */	
	public ResponseData patchDataOnServer(boolean synchronous,Class<?> resultClass) throws IllegalStateException
	{			
		return this.drupalClient.patchObject(this,resultClass, RequestMethod.PATCH, this, synchronous);
	}
			
	public Object getPatchObject()
	{		
		ObjectComparator comparator = new ObjectComparator();
		FootPrint currentState = comparator.createFootPrint(this.getManagedData());
		
		@SuppressWarnings("null")
		Object difference = this.getDifference(this.footprint, currentState, comparator);
		if(difference != null)
		{			
			return difference;
		}else{
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
		
	private boolean canPatch(@NonNull FootPrint origin,@NonNull FootPrint current,@NonNull ObjectComparator comparator)
	{
		Object difference = this.getDifference(origin, current, comparator);
		return (difference != null);
	}
	

	private Object getDifference(@NonNull FootPrint origin,@NonNull FootPrint current,ObjectComparator comparator)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);	
		Assert.assertNotNull("You have to make initial objects footprint in order to calculate changes",origin);
		return comparator.getDifferencesJSON(origin, current);
	}
	
	/**
	 * Method can be overriden in order to specify non-default charset for Entity.
	 */
	@Override
	public String getCharset()
	{		
		return null;
	}
		
	abstract @NonNull Object getManagedData();	
}
