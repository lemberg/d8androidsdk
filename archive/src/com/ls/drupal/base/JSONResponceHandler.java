package com.ls.drupal.base;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ls.drupal.base.annotations.ResponceClass;
import com.ls.drupal.base.annotations.ResponceCollectionClass;
import com.ls.drupal.base.annotations.ResponceMapClass;

class JSONResponceHandler<T extends ResponceItem> extends ResponceHandler<T>
{

	private Class<T> classT;

	public List<T> getItemsFromResponce(String responce)
	{
		final List<T> result = new LinkedList<T>();
		try
		{
			JSONObject jsonObj = new JSONObject(responce);

			result.add(itemFromJSON(jsonObj));// TODO implement array
												// check/handling
			// if (jsonObj instanceof JSONArray) {
			//
			// } else {
			// result.add(itemFromJSON(jsonObj));
			// }

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private T itemFromJSON(JSONObject json)
	{
		T result = null;
		try
		{
			result = classT.newInstance();
			result.initWithJson(json);
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Method initialises object fields with appropriate values. NOTE: only one-level Maps and collections are supported. Arrays aren't supported.
	 * @param key
	 * @param source
	 * @param allFields
	 * @param dest
	 */
	public static void applyJSONValueForKey(String key, JSONObject source, List<Field> allFields, Object dest)
	{
		for (Field field : allFields)
		{
			if (field.getName().equalsIgnoreCase(key))
			{
				Class<?> type = field.getType();
				field.setAccessible(true);
				try
				{
					if (type.equals(long.class))
					{
						long value = source.getLong(key);
						field.setLong(dest, value);
					}
					if (type.equals(int.class))
					{
						int value = source.getInt(key);
						field.setInt(dest, value);
					}
					if (type.equals(float.class))
					{
						float value = Double.valueOf(source.getDouble(key)).floatValue();
						field.setFloat(dest, value);
					} else if (type.equals(double.class))
					{
						double value = source.getDouble(key);
						field.setDouble(dest, value);
					} else if (type.equals(boolean.class))
					{
						boolean value = source.getBoolean(key);
						field.setBoolean(dest, value);
					} else if (type.equals(byte.class))
					{
						byte value = Integer.valueOf(source.getInt(key)).byteValue();
						field.setByte(dest, value);
					} else if (type.equals(short.class))
					{
						short value = Integer.valueOf(source.getInt(key)).shortValue();
						field.setShort(dest, value);
					} else if (type.equals(char.class))
					{
						String strValue = source.getString(key);
						if (strValue.length() > 0)
						{
							char value = strValue.charAt(0);
							field.setChar(dest, value);
						}
					} else
					{// Handle object type

						ResponceClass classAnnot = field.getAnnotation(ResponceClass.class);
						if(classAnnot != null)
						{
							type = classAnnot.type();
						}
						
						Object value = objectFromJSONObject(key, source, type, field);
						if (value != null)
						{
							field.set(dest, value);
						} else
						{
							Log.w(JSONResponceHandler.class.getName(), "Field:" + field.getName() + " of type:" + type.getName()
									+ " can't be handled automatically");
						}
					}

				} catch (JSONException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (InstantiationException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static Object objectFromJSONObject(String key, JSONObject source, Class<?> type, Field field) throws InstantiationException,
			IllegalAccessException, JSONException
	{
		if (type == null)
		{
			type = field.getType();
		}

		if (type.equals(Long.TYPE)) // Long
		{
			return source.getLong(key);
		}
		if (type.equals(Integer.TYPE)) // Integer
		{
			return source.getInt(key);
		}

		if (type.equals(Float.TYPE))
		{
			return Double.valueOf(source.getDouble(key)).floatValue();
		}

		if (type.equals(Double.TYPE)) // Double
		{
			return source.getDouble(key);
		}

		if (type.equals(Boolean.TYPE)) // Boolean
		{
			return source.getBoolean(key);
		}

		if (type.equals(Byte.TYPE)) // Byte
		{
			return Integer.valueOf(source.getInt(key)).byteValue();
		}

		if (type.equals(Short.TYPE)) // Short
		{
			return Integer.valueOf(source.getInt(key)).shortValue();
		}

		if (type.equals(Character.TYPE)) // Char
		{
			String strValue = source.getString(key);
			if (strValue.length() > 0)
			{
				return strValue.charAt(0);
			}
		}

		if (type.equals(String.class)) // String
		{
			return source.getString(key);
		}

		if (ResponceItem.class.isAssignableFrom(type))// Handle custom class
		{
			ResponceItem value = (ResponceItem) type.newInstance();
			value.initWithJson(source.getJSONObject(key));
			return value;
		}

		if (Map.class.isAssignableFrom(type))
		{
			return mapFromJSONObj(field, source.getJSONObject(key));
		}

		if (Collection.class.isAssignableFrom(type))
		{
			return collectionFromJSONArray(field, source.getJSONArray(key));
		}
		
		return null;
	}

	private static Object objectFromJSONArray(int position, JSONArray source, Class<?> type, Field field) throws InstantiationException,
			IllegalAccessException, JSONException
	{
		if (type == null)
		{
			type = field.getType();
		}

		if (type.equals(Long.TYPE)) // Long
		{
			return source.getLong(position);
		}
		if (type.equals(Integer.TYPE)) // Integer
		{
			return source.getInt(position);
		}

		if (type.equals(Float.TYPE))
		{
			return Double.valueOf(source.getDouble(position)).floatValue();
		}

		if (type.equals(Double.TYPE)) // Double
		{
			return source.getDouble(position);
		}

		if (type.equals(Boolean.TYPE)) // Boolean
		{
			return source.getBoolean(position);
		}

		if (type.equals(Byte.TYPE)) // Byte
		{
			return Integer.valueOf(source.getInt(position)).byteValue();
		}

		if (type.equals(Short.TYPE)) // Short
		{
			return Integer.valueOf(source.getInt(position)).shortValue();
		}

		if (type.equals(Character.TYPE)) // Char
		{
			String strValue = source.getString(position);
			if (strValue.length() > 0)
			{
				return strValue.charAt(0);
			}
		}

		if (type.equals(String.class)) // String
		{
			return source.getString(position);
		}

		if (ResponceItem.class.isAssignableFrom(type))// Handle custom class
		{
			ResponceItem value = (ResponceItem) type.newInstance();
			value.initWithJson(source.getJSONObject(position));
			return value;
		}

		if (Map.class.isAssignableFrom(type))
		{
			return mapFromJSONObj(field, source.getJSONObject(position));
		}
		
		if (Collection.class.isAssignableFrom(type))
		{
			return collectionFromJSONArray(field, source.getJSONArray(position));
		}
		return null;
	}

	private final static Map mapFromJSONObj(Field field, JSONObject obj) throws InstantiationException, IllegalAccessException, JSONException
	{
		if (field == null)
		{
			return null;
		}

		Class<?> type = field.getType();
		ResponceMapClass mapClass = field.getAnnotation(ResponceMapClass.class);
		if (mapClass != null)
		{
			Map result = (Map) type.newInstance();
			JSONArray keys = obj.names();
			if (keys != null)
			{
				for (int counter = 0; counter < keys.length(); counter++)
				{
					// We can support one-level collections only, so next
					// iteration won't gain field value
					Object key = objectFromJSONArray(counter, keys, mapClass.keyType(), null);
					Object value = objectFromJSONObject(keys.getString(counter), obj, mapClass.valueType(), null);
					result.put(key, value);
				}
			}
			return result;
		} else
		{
			return null;
		}
	}

	private final static Collection collectionFromJSONArray(Field field, JSONArray obj) throws InstantiationException, IllegalAccessException, JSONException
	{
		if (field == null)
		{
			return null;
		}

		Class<?> type = field.getType();
		ResponceCollectionClass collectionClass = field.getAnnotation(ResponceCollectionClass.class);
		if (collectionClass != null)
		{
			Collection result = (Collection) type.newInstance();

			for (int counter = 0; counter < obj.length(); counter++)
			{
				// We can support one-level collections only, so next iteration
				// won't gain field value
				Object value = objectFromJSONArray(counter, obj, collectionClass.entryType(), null);				
				result.add(value);
			}

			return result;
		} else
		{
			return null;
		}
	}
}
