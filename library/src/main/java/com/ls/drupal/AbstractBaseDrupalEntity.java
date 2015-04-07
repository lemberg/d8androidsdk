

/*
 * The MIT License (MIT)
 *  Copyright (c) 2014 Lemberg Solutions Limited
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ls.drupal;

import com.google.gson.annotations.Expose;

import com.android.volley.VolleyError;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.http.base.ICharsetItem;
import com.ls.http.base.ResponseData;
import com.ls.util.ObjectComparator;
import com.ls.util.ObjectComparator.Snapshot;
import com.ls.util.VolleyResponceUtils;

import junit.framework.Assert;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBaseDrupalEntity implements DrupalClient.OnResponseListener, ICharsetItem
{
	transient private DrupalClient drupalClient; 

	transient private Snapshot snapshot;

	/**
	 * In case of request canceling - no method will be triggered.
	 * 
	 * @author lemberg
	 * 
	 */
	public interface OnEntityRequestListener
	{
		void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data);
		void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, VolleyError error);
		void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag);
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
	 * @param method is instance od {@link com.ls.http.base.BaseRequest.RequestMethod} enum, this method is called for. it can be "GET", "POST", "PATCH" or "DELETE".
	 * @return parameters for the request method specified.
	 */
	protected abstract Map<String, String> getItemRequestGetParameters(RequestMethod method);

	/** Get data object, used to perform perform get/post/patch/delete requests.
	 * @return data object. Can implement {@link com.ls.http.base.IPostableItem} or {@link com.ls.http.base.IResponseItem} in order to handle json/xml serialization/deserialization manually.
	 */
	abstract @NonNull
    Object getManagedData();

    /**
     * @param method is instance od {@link com.ls.http.base.BaseRequest.RequestMethod} enum, this method is called for. it can be "GET", "POST", "PATCH" or "DELETE".
     * @return headers for the request method specified.
     */
    protected Map<String, String> getItemRequestHeaders(RequestMethod method){
        return new HashMap<String, String>();
    };

	@Override
	public String getCharset()
	{
		return null;
	}

	public AbstractBaseDrupalEntity(DrupalClient client)
	{
		this.drupalClient = client;	
	}
	
	
	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass
	 *            class of result or null if no result needed.
	 * @param tag Object tag, passer to listener after request was finished or failed because of exception
	 * @param listener 
	 * @return @class ResponseData entity, containing server response string and
	 *         code or error in case of synchronous request, null otherwise
	 */
	public ResponseData pushToServer(boolean synchronous, Class<?> resultClass, Object tag, OnEntityRequestListener listener)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		DrupalEntityTag drupalTag = new DrupalEntityTag(false, tag, listener);
		ResponseData result = this.drupalClient.postObject(this, resultClass, drupalTag, this, synchronous);		
		return result;
	}

	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param tag Object tag, passer to listener after request was finished or failed because of exception
	 * @param listener 
	 * @return @class ResponseData entity, containing server response or error
	 *         in case of synchronous request, null otherwise
	 */
	public ResponseData pullFromServer(boolean synchronous, Object tag, OnEntityRequestListener listener)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		DrupalEntityTag drupalTag = new DrupalEntityTag(true, tag, listener);
		ResponseData result;
        Map postParams = this.getItemRequestPostParameters();
        if(postParams == null || postParams.isEmpty()) {
            result = this.drupalClient.getObject(this, this.getManagedDataClassSpecifyer(), drupalTag, this, synchronous);
        }else{
            result = this.drupalClient.postObject(this, this.getManagedDataClassSpecifyer(), drupalTag, this, synchronous);
        }
		;		
		return result;
	}

	/**
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass
	 *            class of result or null if no result needed. 
	 * @param tag Object tag, passer to listener after request was finished or failed because of exception
	 * @param listener 
	 * @return @class ResponseData entity, containing server response or error
	 *         in case of synchronous request, null otherwise
	 */
	public ResponseData deleteFromServer(boolean synchronous, Class<?> resultClass, Object tag, OnEntityRequestListener listener)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		DrupalEntityTag drupalTag = new DrupalEntityTag(false, tag, listener);
		ResponseData result = this.drupalClient.deleteObject(this, resultClass, drupalTag, this, synchronous);		
		return result;
	}

	// OnResponseListener methods
	
	@Override
	public void onResponseReceived(ResponseData data, Object tag)
	{		
		DrupalEntityTag entityTag = (DrupalEntityTag)tag;
		if (entityTag.consumeResponse)
		{
			this.consumeObject(data.getData());
		}

		if(entityTag.listener != null)
		{
			entityTag.listener.onRequestCompleted(this, entityTag.requestTag, data);
		}
        ConnectionManager.instance().setConnected(true);
	}
	
	@Override
	public void onError(VolleyError error, Object tag)
	{
		DrupalEntityTag entityTag = (DrupalEntityTag)tag;
		if(entityTag.listener != null)
		{
			entityTag.listener.onRequestFailed(this,entityTag.requestTag,error);
		}

        if(VolleyResponceUtils.isNetworkingError(error))
        {
            ConnectionManager.instance().setConnected(false);
        }
	}

	@Override
	public void onCancel(Object tag)
	{
		DrupalEntityTag entityTag = (DrupalEntityTag)tag;
		if(entityTag.listener != null)
		{
			entityTag.listener.onRequestCanceled(this,entityTag.requestTag);
		}	
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

	// Request canceling
	public void cancellAllRequests()
	{
		this.drupalClient.cancelAllRequestsForListener(this, null);
	}

	// Patch method management

	/**
	 * Creates snapshot to be used later in order to calculate differences for
	 * patch request.
	 */
	public void createSnapshot()
	{
		this.snapshot = getCurrentStateSnapshot();
	}
	
	/**
	 * Release current snapshot (is recommended to perform after successful patch request)
	 */
	public void clearSnapshot()
	{
		this.snapshot = null;
	}

	protected Snapshot getCurrentStateSnapshot()
	{
		ObjectComparator comparator = new ObjectComparator();
		return comparator.createSnapshot(this.getManagedDataChecked());
	}

	/**
	 * 
	 * @param synchronous
	 *            if true - request will be performed synchronously.
	 * @param resultClass
	 *            class of result or null if no result needed.
	 * @param tag Object tag, passer to listener after request was finished or failed because of exception
	 * @param listener 
	 * @return @class ResponseData entity, containing server response and
	 *         resultClass instance or error in case of synchronous request,
	 *         null otherwise
	 * @throws IllegalStateException
	 *             in case if there are no changes to post. You can check if
	 *             there are ones, calling <code>isModified()</code> method.
	 */
	public ResponseData patchServerData(boolean synchronous, Class<?> resultClass, Object tag, OnEntityRequestListener listener) throws IllegalStateException
	{
		DrupalEntityTag drupalTag = new DrupalEntityTag(false, tag, listener);
		ResponseData result = this.drupalClient.patchObject(this, resultClass, drupalTag, this, synchronous);
		return result;
	}

	public Object getPatchObject()
	{
		ObjectComparator comparator = new ObjectComparator();
		Snapshot currentState = comparator.createSnapshot(this.getManagedDataChecked());

		@SuppressWarnings("null")
        Object difference = this.getDifference(this.snapshot, currentState, comparator);
		if (difference != null)
		{
			return difference;
		} else
		{
			throw new IllegalStateException("There are no changes to post, check isModified() call before");
		}
	}

	/**
	 * @return true if there are changes to post, false otherwise
	 */
	@SuppressWarnings("null")
	public boolean isModified()
	{
		ObjectComparator comparator = new ObjectComparator();
		return this.isModified(this.snapshot, comparator.createSnapshot(this), comparator);
	}

	private boolean isModified(@NonNull Snapshot origin, @NonNull Snapshot current, @NonNull ObjectComparator comparator)
	{
		Object difference = this.getDifference(origin, current, comparator);
		return (difference != null);
	}

	private Object getDifference(@NonNull Snapshot origin, @NonNull Snapshot current, ObjectComparator comparator)
	{
		Assert.assertNotNull("You have to specify drupal client in order to perform requests", this.drupalClient);
		Assert.assertNotNull("You have to make initial objects snapshot in order to calculate changes", origin);
		return comparator.getDifferencesJSON(origin, current);
	}
	
	@NonNull
	private Object getManagedDataChecked()
	{
		Assert.assertNotNull("You have to specify non null data object", this.getManagedData());
		return getManagedData();
	}
	
	protected final class DrupalEntityTag
	{
		public OnEntityRequestListener listener;
		public Object requestTag;
		public boolean  consumeResponse;
		
		public DrupalEntityTag(boolean consumeResponse,Object requestTag,OnEntityRequestListener listener)
		{
			this.consumeResponse = consumeResponse;
			this.requestTag = requestTag;
			this.listener = listener;
		}
	}
}
