# sinch-java-sdk
[![Maven Central](https://img.shields.io/maven-central/v/com.sinch.sdk/java-sdk.svg)](https://mvnrepository.com/artifact/com.sinch.sdk/java-sdk)

## Documentation
Sign up and find API documentation on the [Developer page](https://developers.sinch.com/)

## Installation
The artifacts can be found on [Maven Central](https://mvnrepository.com/artifact/com.sinch.sdk/java-sdk).

#### Gradle setup
```groovy
dependencies {
    implementation "com.sinch.sdk:java-sdk:${sdkVersion}"
    ...
}
```

#### Maven setup
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

## Quickstart
Start by visiting the [Getting started guide](https://dashboard.sinch.com/convapi/getting-started)

### Initialize the client
Find your Credentials [here](https://dashboard.sinch.com/settings/access-keys)

>This step is not needed if the Credentials are injected as described [here](#external-configuration), we strongly
recommend that you do.
```java
String keyId="__key_id__";
String keySecret="__key_secret__";
String projectId="__project_id__";

Sinch.init(keyId, keySecret, projectId);
```

### Find the app id
Find information about your apps on the [dashboard](https://dashboard.sinch.com/convapi/apps), or with the help of the SDK
```java
Sinch.conversationApi(Region.EU).apps().list();
```

### Send a text message
```java
Messages messages =
    Sinch.conversationApi(Region.EU) // The region of the app
        .messages("__app_id__");

messages.send(new TextMessageRequest("Hi from the Sinch SDK!")
    .smsRecipient("+46707654321"));
```

### List conversations
```java
Sinch.conversationApi(Region.EU).conversations()
    .listConversations(new ListConversationsParams().appId("__app_id__"));
```
Find more examples [here](src/test/java/example/conversationapi/)

## SDK Versions
Project in this repository follow the grammar defined in [Semantic Versioning](https://semver.org/spec/v2.0.0.html) (`MAJOR.MINOR.PATCH`) with Spring framework's interpretation of versioning scheme.

`MAJOR` may involve a significant amount of work to upgrade.

`MINOR` should involve little to no work to upgrade.

`PATCH` should involve no work.

## Java Versions
`Java 8` defaults to the OkHttpClient (https://square.github.io/okhttp).

`Java 11` defaults to the Java native HttpClient.

## Custom http clients

The SDK has built-in support for:
- [OkHttpClient](https://square.github.io/okhttp/)
- [Apache client](https://hc.apache.org/httpcomponents-client-5.0.x/index.html)
- JDK 11 HttpClient (if the SDK is run on Java 11)

In order to use your own instance of a HttpClient it must be provided explicitly:

### Apache
```java
Sinch.conversationApi(Region.EU, () -> new ApacheHttpRestClientFactory(existingApacheHttpClient));
```

You can also implement your own SinchRestClient for the http client currently in use:
```java
class CustomRestClientFactory implements SinchRestClientFactory {
  @Override
  public SinchRestClient getClient(
      final Duration requestTimeout, final ObjectMapper objectMapper) {
    // return the implementation of SinchRestClient here using the custom http client
  }
```
The `requestTimeout` value is read from the VM property `sinch.http_timeout`, it will return `null` if missing.
Read more about external configuration [here](#external-configuration)

This custom client factory can then be used as in the `Apache` example above.

### Excluding the built-in client
The SDK has a dependency on OkHttp in order to run with a default HttpClient on Java 8.
We strongly recommend excluding this transitive dependency if running on Java 11 or using a different http client.

#### Gradle
```groovy
dependencies {
    implementation("com.sinch.sdk:java-sdk:${sdkVersion}") {
        exclude group: 'com.squareup.okhttp3', module: 'okhttp'
    }
    implementation "org.apache.httpcomponents.client5:httpclient5:${apacheHttpClientVersion}"
    ...
}
```

#### Maven
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

## External configuration
The Sinch SDK can be configured using system properties.

### Usage
There are many ways of sending system properties to your application.
The easiest way is to simply pass it from the command line:
```bash
java -Dsinch.project_id="__project_id__" -jar app.jar
```

### Available settings
#### General settings
Find your Credentials at https://dashboard.sinch.com/settings/access-keys
```properties
sinch.project_id # Project id from the link above (String)
sinch.key_id # Key id from the link above (String)
sinch.key_secret # Key secret from the link above (String)
sinch.http_timeout # Timeout in seconds of the http client used to access the api (Integer)
```

## Logging
The SDK uses Slf4j for logging.

Consult the [documentation](http://slf4j.org/docs.html) for information about logging configuration.

`INFO`
- Configuration settings (non-sensitive data e.g. url)

`ERROR`
- Responses which finish with 4xx or 5xx status code

`DEBUG`
- Access token actions (refresh, response received)
- Requests method and URI
- Response status code and body

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