
# sinch-java-sdk

Build project using ./gradlew build

A built jar can be found in the build/libs folder

## Usage
The typical initialization for SDK is:
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
      final Duration requestTimeout, final ObjectMapper objectMapper) {
    // return the implementation of SinchRestClient here with usage of custom http client
  }
```
The `requestTimeout` value is read from Java VM parameter `sinch.http_timeout`. The value must be defined in seconds.
If parameter is missing (or its value) then `requestTimeout` is equal to `null`.

Such factory must be used later in order to create a Sinch Conv API client:
```
Sinch.conversationApi(Region.EU, () -> new CustomRestClientFactory())
```

## Logs

### INFO Level
- configuration settings (non-sensitive data e.g. url)

### ERROR Level
- responses which finish with 4xx or 5xx status code

### DEBUG Level
- access token actions (refresh, response received)
- requests method and URI
- response status code and body

## Architecture Decision Log

### ADR-01: Provide support for different HTTP Clients

#### Context
The user of the SDK may have its own instance of HTTP client with metrics already attached to it. 
We should provide a user the possibility to use its own client instance or use the one he prefers.

#### Decision
In order to support this possibility we must provide different implementations which 
uses different HTTP client under the hood. We will support `JDK11 HttpClient`, `OkHttpClient` and `Apache HttpClient`.
We must also provide a possibility for the user to use another client not supported by us (by providing its own `SinchRestClient` implementation).