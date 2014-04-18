package com.ls.drupal.base;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponceItem
{


	public void initWithXMLString(String XMlStr)
	{

	}
	
	//JSON handling ------------------------------------------------------------------------------------ 
	
	
	public void initWithJson(JSONObject json)
	{
		List<Field> allFields = this.getAllFields(new LinkedList<Field>(), this.getClass());
		JSONArray keys = json.names();
		if(keys != null)
		{
			for(int counter = 0; counter < keys.length();counter++)
			{
				try
				{
					JSONResponceHandler.applyJSONValueForKey(keys.getString(counter), json, allFields, this);
				} catch (JSONException e)
				{					
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Method is responsible for setting. Subclasses can override it in order to
	 * handle setting manually
	 * 
	 * @param key
	 *            name of value to be set in to object
	 * @param source
	 *            object to fetch value from
	 * @return true if value was set, false if setting has to be performed
	 *         automatically
	 */
	protected boolean applyJSONValueForKey(String key, JSONObject source)
	{
		return false;
	}

	private static List<Field> getAllFields(List<Field> fields, Class<?> type)
	{
		for (Field field : type.getDeclaredFields())
		{
			fields.add(field);
		}

		if (type.getSuperclass() != null)
		{
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}
}
