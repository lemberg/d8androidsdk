## Introduction

Lemberg Drupal API is a library for native Android applications to communicate with Drupal web servers. 

Currently library is using google volley and gson libraries for communication with server and object serialization/deserialization. 

Main purpose of this library is to make communication with Drupal 8 - based servers as easy and intuitive as possible. 


## Main features

### 1. Requests are binded to entities

You can simply call

- `DrupalEntity.pushToServer()` to post data to server.
- `DrupalEntity.pullFromServer()` to pull data from server.
- `DrupalEntity.deleteFromServer()` to remove data from server.
- `DrupalEntity.patchServerData()` to post  patch data to server.

### 2. Requests are not binded to entities only

Besides of entity api provides few more handy structures:

`DrupalArrayEntity` container, implementing collection interface. Can manage drupal and non-drupal entities, providing them with all `DrupalEntity` methods like post, push, pull,  delete. 

 **Note:** in case of raw content item types deserialization issues may occur.

`DrupalEntityContainer` wrapper object, able to contain drupal or non-drupal entities providing them with all `DrupalEntity` methods like post, push, pull,  delete.

**Note:** in case of raw content item types deserialization issues may occur. You shouldn’t store instances of this class (or subclasses) as serializable fields: objects, containing subclasses of this EntityContainer won’t be able to deserialize them correctly.

### 3. API can calculate object differences to perform patch requests

Callback is quite simple: 

```java
DrupalEntity.createSnapshot();// to fix initial object state.
//Perform some changes.
DrupalEntity.isModified();// check if there are changes to patch
DrupalEntity.patchServerData();// to post patch to server is there where some changes.
```
**Tip:** There is no point in patch call if there are no changes.

### 4. Synchronous and asynchronous requests

Google volley API provides asynchronous API calls only, which can cause troubles and complicated code in case if you need sequential and related API calls. The only solution was to use future task for that purpose. But using our API you can just pass boolean value, telling if you want to perform synchronous request and get result right now or prefer asynchronous request.

**a) Asynchronous request**
In order to handle asynchronous request result you can pass

`OnEntityRequestListener` object while triggering request . It provides following methods:

```java
void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data);//called after successful request.

void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag);//called after successful entity deleterequest.

void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, VolleyError error);
```

**Note:** in case of pull request entity will be updated with response data automatically.
and set `RequestProgressListener` to `DrupalClient` to track pending requests:

```java
void onRequestStarted(DrupalClient theClient, int activeRequests);
void onRequestFinished(DrupalClient theClient, int activeRequests);
```

**b) Synchronous request**
Result object will be returned immediately. 

**Note:** listener methods will also be triggered.

### 5. Flexible object serialization/deserialization

You can: 

 - rely on default gson serialization
 - configure gson object, used for serialization/deserialization:
```java
 GsonBuilder builder = SharedGson.getBuilder();
 //configure builder
 SharedGson.performUpdates();
```

**Note:** `SharedGson` updates between `DrupalEntity.createSnapshot()` and `DrupalEntity.patchServerData()` calls can corrupt difference calculation.

 - implement `IPostableItem`, `IResponceItem` and `ICharsetItem` to provide custom object 

**Note:** if object, containing `IPostableItem` or `IResponceItem` as field is (de)serialized with default method (doesn’t implement this interfaces) - interfaces will be ignored. 
`ICharsetItem`  is called for root object only (field’s charset interface won’t be called).

## Other details

### ResponseData

Object, containing:

 - original response string;
 - response headers;
 - response status code
 - deserialized data object, if class was specified. Each `pushToServer`, `pullFromServer` and `deleteFromServer` allows user to specify desired response data object Type to be parsed from server response.
 - `VolleyError` (in case if some error occurred)

### DrupalClient
Object, containing server base URL, `LoginManager` and `RequestQueue` and responsible for server request generation and posting to server. You have to specify `DrupalClient` while creating `DrupalEntity` instance.

### Performing request
You can perform custom request, using drupal client directly but it’s recommended to use *DriupalEntities* instead.

### Request canceling
Also you can use client instance to cancel all pending requests for the screen: `DrupalClient.cancelAll();`
Or by tag/listener, if you’ve started some manually: `DrupalClient.cancelAllRequestsForListener(OnResponseListener,tag);`

**Note:** if you want to cancel all requests, related to specific `DrupalEntity`, just call `DrupalEntity.cancellAllRequests().`

### LoginManager

Object, implemening `ILoginManager` is responsible for applying necessary authentication data to the requests. Can be shared between multiple `DrupalClients`.

In case if no such data is required - `AnonymousLoginManager` can be passed to drupal client.

## Code samples

### 1. Implement DrupalEntity:

In order to implement drupa entity you just have to extend `AbstractDrupalEntity` or `AbstractDrupalArrayEntity` and implement abstract methods: 

`getPath`, this method will return relative path to entity on the server.

```java
@Override
protected String getPath()
{		
    return "entities/" + entityId;
}
```

`getItemRequestPostParameters` this method will return item post parameters if needed.

**Note:** method will be called for post request only and in case if null is returned - serialized object will be posted.

```java
@Override
protected Map<String, String> getItemRequestPostParameters()
{		
    return null;
}
```

`getItemRequestGetParameters` this method can be called for get/post/delete or patch requests and you can define parameters for each request type:

```java
@Override
protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
{
    switch (method) {
        case GET:
			Map<String, String> result = new HashMap<String, String>();
			result.put("page",this.pageNumber);
			return result;	
		case POST:
		case PATCH:
		case DELETE:		
		default:
			return null;			
	}
}
```

### 2. Create Drupal client


`DrupalClient client = new DrupalClient("http:\\my.server.com", this.getContext());`

You can also use alternative constructors to specify request queue or login manager.

### 3. Instantiate Drupal Entity

```java
MyDrupalEntity entity = new MyDrupalEntity(client);
entity.setPagenumber(104);
```

### 4. Perform server requests

#### 4.1 Asynchronous

```java
boolean synchronous = false;
Class<?> myResponseClass = MyResponceClass.class;
final Tag myTag = new Tag();
OnEntityRequestListener listener = new OnEntityRequestListener()
{
    void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data)
    {
	    if(tag == myTag)
        {
		    //Handle response;
        }
    }	
 	void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag)
    {
    	if(tag == myTag)
         {
	    	//Handle response cancel;
        }
    }
    void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, VolleyError error)
    {
    	if(tag == myTag)
        {
	    	//Handle response failure;
        }
    }
}
```

**a) Fetch entity from server**

```java
entity.pullFromServer(synchronous, listener, myTag );
```

**b) Post entity to server**

```java
entity.pushToServer(synchronous, myResponseClass , listener, myTag );
```

**c) Patch entity on server**

```java
entity.createSnapshot();
entity.setValue1(...);
entity.setValie2(...);
if(entity.canPatch()) {
	entity.patchServerData(synchronous, myResponseClass , listener, myTag );
}
```

**d) Remove entity from server**

```java
entity.deleteFromServer(synchronous, myResponseClass , listener, myTag );
```

#### 4.2 Synchronous

```java
boolean synchronous = false;
Class<?> myResponseClass = MyResponceClass.class;
```

**a) Fetch entity from server**

```java
ResponseData response = entity.pullFromServer(synchronous, null, null);
```

**b) Post entity to server**

```java
ResponseData response = entity.pushToServer(synchronous, myResponseClass , null, null);
```

**c) Patch entity on server**

```java
entity.createSnapshot();
entity.setValue1(...);
entity.setValie2(...);
if(entity.isModified()) {
    ResponseData response = entity.patchServerData(synchronous, myResponseClass , null, null);
}
```

**d) Remove entity from server**

```java
ResponseData response = entity.deleteFromServer(synchronous, myResponseClass , null, null);
```

#### 5.Track active requests count

```java
client.setProgressListener(new RequestProgressListener()
{				
    @Override
    public void onRequestStarted(DrupalClient theClient, int activeRequests)
	{
		if(activeRequests > 0)
        {
        	showProgress();
		}					
	}
				
	@Override
    public void onRequestFinished(DrupalClient theClient, int activeRequests)
	{
		if(activeRequests == 0)
        {
        	hideProgress();
		}			
	}
});
```
