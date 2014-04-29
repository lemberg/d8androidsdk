package com.ls.http.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SharedGson
{
	static{
		init();
	}
	private static Gson gson ;	
	
	private static void init()
	{
		GsonBuilder builder = new GsonBuilder();
//		builder.registerTypeAdapterFactory(new LSAdapterFactory());
		gson = builder.create();
	}
	
	public static Gson getGson()
	{
		return gson;
	}	
	
//	private static class LSAdapterFactory implements TypeAdapterFactory
//	{		
//		@Override
//		public <T> TypeAdapter<T> create(Gson arg0, TypeToken<T> type)
//		{			
////			Log.e("SharedGson","type:"+type.toString()+" RAW type:"+type.getRawType().toString()+" class:"+type.getClass().toString());
//			if (!Collection.class.isAssignableFrom(type.getRawType())){		       
//			      return null;
//			}
//			
//			TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
//			  
//			return new LSCollectionAdapter<T>(delegate, type);
//		}
//		
//	}
//	
//	static class LSCollectionAdapter<T> extends TypeAdapter<T> {
//
//		private final TypeAdapter<T> delegate;
//		private final TypeToken<T> type;
//		
//		public LSCollectionAdapter(TypeAdapter<T> theDelegate,TypeToken<T> theType)
//		{
//			this.delegate = theDelegate;
//			this.type = theType;
//		}
//		
//		@SuppressWarnings("unchecked")
//		@Override
//		public T read(JsonReader arg0) throws IOException
//		{				
//			Collection list = (Collection) delegate.read(arg0);
//			
//			T item = (T)ObjectsFactory.newInstance(this.type.getRawType());
//			Log.d("SharedGson","result types:"+ item.getClass().getTypeParameters()[0].getName());
//			Log.d("SharedGson","item:"+item+" list:"+list);
//				((Collection)item).addAll(list);			
//			return item;
//		}
//
//		@Override
//		public void write(JsonWriter arg0, T arg1) throws IOException
//		{
//			this.delegate.write(arg0, arg1);			
//		}
//	   
//	   
//	  }
}
