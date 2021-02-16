
# sinch-java-sdk

Build project using ./gradlew build

A built jar can be found in the build/libs folder

# Usage
The typical initialization for SDK is:<br/>
```
Sinch.init(keyId, keySecret, projectId);
ConversationApiClient apiClient = Sinch.conversationApi(Region.US);
```
This will use newly created HttpClient shipped with JDK 11. It is possible to change it just by calling another constructor.
SDK currently provides the support for two Http clients:
1. HttpClient from JDK 11
2. OkHttpClient (https://square.github.io/okhttp/)
3. Apache HttpClient (https://hc.apache.org/httpcomponents-client-5.0.x/index.html)

It is possible to create Sinch API Client by providing your own instance of Http client. Example:
```
var httpClient = new OkHttpClient(); 
Sinch.init(keyId, keySecret, projectId);
ConversationApi apiClient = Sinch.conversationApi(Region.US, () -> SinchRestClientFactory.create(httpClient));
```
A user can also use Http Client which Sinch does not support by default:
```
class CustomRestClientFactory implements SinchRestClientFactory {

  @Override
  public SinchRestClient getClient(
      final AuthenticationService authenticationService, final ObjectMapper objectMapper) {
    // return the implementation of SinchRestClient here with usage of custom http client
  }
```
Important to notice here is that in order to read values returned by Sinch Conv API you must use the 
objectMapper which is provided in the arguments to the **_getClient_** and in each request you must
send also the Authorization header with bearer token provided by **_authenticationService.getHeaderValue()_**.

Such factory must be used later in order to create a Sinch Conv API client:
```
Sinch.conversationApi(Region.EU, () -> new CustomRestClientFactory())
```

# Architecture Decision Log

1. <br/>
**Title:** HTTP client that SDK should use to call ConvApi.<br />
**Context:** The user of the SDK may have its own instance of HTTP client with metrics already attached to it. 
We should provide a user the possibility to use its own client instance or use the one he prefers.<br />
**Decision:** In order to support this possibility we must provide different implementations which 
uses different HTTP client under the hood. We will support JDK11 HttpClient, OkHttpClient and Apache HttpClient.
We must also provide a possibility for the user to use another client not supported by us (by providing its own SinchRestClient implementation).