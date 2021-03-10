
# sinch-java-sdk

## How to build the project 

Build project using ./gradlew build

A built jar can be found in the build/libs folder

## How to use SDK in a project

### Default Maven setup 

```xml  
<dependencies>
    <dependency>
      <groupId>com.sinch.sdk</groupId>
      <artifactId>java-sdk</artifactId>
      <version>${sdk.version}</version>
    </dependency>
    ...
</dependencies>
```

### Default Gradle setup

```groovy
dependencies {
    implementation "com.sinch.sdk:java-sdk:${sdkVersion}"
    ...
}
```

### Setup with different HttpClient 

In case SDK is run on Java 8 we use by default OkHttpClient (https://square.github.io/okhttp).
In case SDK is run on Java 11 we use by default HttpClient shipped with Java 11.
Another HttpClient implementation might be used (see section [Usage](#usage) for details).

SDK has dependency to OkHttp added in order to run with default HttpClient on Java 8 runtime.
In case you want to run SDK with different HttpClient, or you run SDK on Java 11 with the default one
we strongly recommend excluding this transitive dependency

### Recommended Maven setup for non-default HttpClient (e.g. Apache)

```xml  
<dependencies>
  <dependency>
    <groupId>com.sinch.sdk</groupId>
    <artifactId>java-sdk</artifactId>
    <version>${sdk.version}</version>
    <exclusions>
      <exclusion>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
  <dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
    <version>${apache.http.version}</version>
  </dependency>
  ...
</dependencies>
```

### Recommended Gradle setup for non-default HttpClient (e.g. Apache)

```groovy
dependencies {
    implementation("com.sinch.sdk:java-sdk:${sdkVersion}") {
        exclude group: 'com.squareup.okhttp3', module: 'okhttp'
    }
    implementation "org.apache.httpcomponents.client5:httpclient5:${apacheHttpClientVersion}"
    ...
}
```

## Usage
The typical initialization for SDK is:
```java
Sinch.init(keyId, keySecret, projectId);
ConversationApiClient apiClient = Sinch.conversationApi(Region.US);
```
This will use default HttpClient based on Java Runtime version:
- OkHttpClient in case SDK is run on Java 8
- HttpClient from JDK 11 in case SDK is run on Java 11

SKD has built-in support for:
- OkHttpClient (https://square.github.io/okhttp/)
- Apache HttpClient (https://hc.apache.org/httpcomponents-client-5.0.x/index.html)
- JDK 11 HttpClient (in case SDK is run on Java 11)

In order to use different built-in HttpClient it must be provided explicitly:
```java
Sinch.init(keyId, keySecret, projectId);
CloseableHttpAsyncClient httpClient = HttpAsyncClientBuilder.create().build();
httpClient.start();
ConversationApi apiClient = Sinch.conversationApi(Region.EU, () -> new ApacheHttpRestClientFactory(httpClient));
```

It is also possible to create Sinch API Client by providing your own instance of Http client. Example for OkHttpClient:
```java
var httpClient = new OkHttpClient();
Sinch.init(keyId, keySecret, projectId);
ConversationApi apiClient = Sinch.conversationApi(Region.US, () -> new OkHttpRestClientFactory(httpClient));
```
A user can also use Http Client which Sinch does not support by default:
```java
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
```java
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

## How to contribute

### New product must be added under package `com.sinch.sdk.api` 
Example: SDK for ConversationAPI is located under package `com.sinch.sdk.api.conversationapi`.

### Use `com.sinch.sdk.api.authentication.AuthenticationService` in order to authenticate calls
AuthenticationService returns header with authentication token which must be used with subsequent rest calls. 
Token is refreshed automatically.

### REST calls should be done via `com.sinch.sdk.restclient.SinchRestClient` 
The instance of SinchRestClient is provided by `com.sinch.sdk.restclient.SinchRestClientFactory` (specific for a customer or default one).

### An easy entry point must be provided for the SDK user
The SDK user should have a simple entry point for specific product part (example `com.sinch.sdk.api.conversationapi.ConversationApi`). 
This entry point must be accessible via Sinch class. The default implementation must be provided but also an easy way to customize it.  
Example (default one):
```java
Sinch.conversationApi(Region.US);
```
Example (customized):
```java
Sinch.conversationApi(Region.US, () -> new OkHttpRestClientFactory(new OkHttpClient()));
```

### Provide builders to construct model classes instead of setters
Example:
```java
new Conversation()
    .active(true)
    .activeChannel(ConversationChannel.MESSENGER)
    .appId(appId)
    .contactId(contactId));
```

### Generate model from product specific Swagger file 
A custom plugin is available to process multiple swagger files for OpenApi.  
Example:
```groovy
generateModelFromOpenApi {
    modelDefinition {
        apiName 'conversationapi'
        url 'https://developers.sinch.com/openapi/5e8c97e796be52003d4ead78'
        prefixesToRemove 'Type', 'V1'
    }
}
```
Teams can add specific `modelDefinition` in order to generate classes. `ApiName` defines the package under `com.sinch.sdk.model`.
`Url` points to the Swagger file. `PrefixesToRemove` is an addition which removes prefixes from generated files (e.g. called with `Type` changes `TypeApp` into `App`).

### Syntax for the SDK user should be as simple as possible 
The most common uses cases should have as short syntax as possible (most hidden by the builders or factories).

### Use Java 8 by default
Java SDK is a multi-release JAR. We use Java 11 mainly to support JDK 11 HttpClient (see [ADR-02: Support for JDK8](#adr-02-support-for-jdk8) and https://openjdk.java.net/jeps/238).

### Document any architecture decisions in [Architecture Decision Log](#architecture-decision-log)

## Architecture Decision Log

### ADR-01: Provide support for different HTTP Clients

#### Context
The user of the SDK may have its own instance of HTTP client with metrics already attached to it. 
We should provide a user the possibility to use its own client instance or use the one he prefers.

#### Decision
In order to support this possibility we must provide different implementations which 
uses different HTTP client under the hood. We will support `JDK11 HttpClient`, `OkHttpClient` and `Apache HttpClient`.
We must also provide a possibility for the user to use another client not supported by us (by providing its own `SinchRestClient` implementation).

### ADR-02: Support for JDK8

#### Context
Most of the market right now runs on Java version 8. As we want to reach as must customers as we can we must support running SDK on Java 8.
However, if someone is already running on Java 11 and wants to use native Java HttpClient then this should also be allowed.

#### Decision
We will provide multi-release JAR (available since Java 9). There is only one additional dependency added especially for Java 8 (OkHttpClient) so this is not a big deal.

#### Consequences
If SDK is run with Java 8 and the default HttpClient then OkHttpClient will be used under the hood.
If SDK is run with Java 11 and the default HttpClient then native JDK HttpClient will be used under the hood.
If SDK is run with Java 11 then a user should exclude the transitive dependency (not required but suggested removing what is not needed). 
A user still can use its own HttpClient by providing custom `SinchRestClientFactory`.