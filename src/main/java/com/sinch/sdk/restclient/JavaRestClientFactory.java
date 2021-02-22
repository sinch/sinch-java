package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.exception.ConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import lombok.SneakyThrows;

public class JavaRestClientFactory implements SinchRestClientFactory {

  private final HttpClient httpClient;

  public JavaRestClientFactory(final HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public SinchRestClient getClient(final Duration requestTimeout, final ObjectMapper objectMapper) {
    return new JavaHttpRestClient(httpClient, requestTimeout, objectMapper);
  }

  private static class JavaHttpRestClient implements SinchRestClient {

    private static final String PATCH = "PATCH";

    private final HttpClient client;
    private final Duration requestTimeout;
    private final ObjectMapper objectMapper;

    JavaHttpRestClient(
        final HttpClient client, final Duration requestTimeout, final ObjectMapper objectMapper) {
      this.client = client;
      this.requestTimeout = requestTimeout;
      this.objectMapper = objectMapper;
    }

    @Override
    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz, Map<String, String> headers) {
      return send(clazz, requestBuilder(uri, headers).thenApply(builder -> builder.GET().build()));
    }

    @Override
    public CompletableFuture<Void> post(URI uri, Map<String, String> headers) {
      return send(requestBuilder(uri, headers)
              .thenApply(builder -> builder.POST(HttpRequest.BodyPublishers.noBody()).build()))
          .thenAccept(res -> {});
    }

    @Override
    public <S> CompletableFuture<Void> post(URI uri, S body, Map<String, String> headers) {
      return send(requestBuilder(uri, headers)
              .thenApply(
                  builder ->
                      builder
                          .POST(
                              HttpRequest.BodyPublishers.ofByteArray(getBytes(objectMapper, body)))
                          .build()))
          .thenAccept(res -> {});
    }

    @Override
    public <T, S> CompletableFuture<T> post(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return send(
          clazz,
          requestBuilder(uri, headers)
              .thenApply(
                  builder ->
                      builder
                          .POST(
                              HttpRequest.BodyPublishers.ofByteArray(getBytes(objectMapper, body)))
                          .build()));
    }

    @Override
    public <T, S> CompletableFuture<T> patch(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return send(
          clazz,
          requestBuilder(uri, headers)
              .thenApply(
                  builder ->
                      builder
                          .method(
                              PATCH,
                              HttpRequest.BodyPublishers.ofByteArray(getBytes(objectMapper, body)))
                          .build()));
    }

    @Override
    public CompletableFuture<Void> delete(URI uri, Map<String, String> headers) {
      return send(requestBuilder(uri, headers).thenApply(builder -> builder.DELETE().build()))
          .thenAccept(res -> {});
    }

    private static HttpResponse<InputStream> validate(final HttpResponse<InputStream> response) {
      final int statusCode = response.statusCode();
      if (statusCode == 401) {
        throw new ConfigurationException(
            "Invalid credentials, verify the keyId and keySecret", bodyToString(response));
      }
      if (statusCode / 100 != 2) {
        throw new ApiException(
            statusCode,
            "Call to " + response.uri() + " received non-success response",
            response.headers(),
            bodyToString(response));
      }
      return response;
    }

    private <T> CompletableFuture<T> send(
        final Class<T> clazz, final CompletableFuture<HttpRequest> request) {
      return send(request)
          .thenApply(HttpResponse::body)
          .thenApply(
              bodyInputStream -> {
                try {
                  return objectMapper.readValue(bodyInputStream, clazz);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
    }

    private CompletableFuture<HttpResponse<InputStream>> send(
        final CompletableFuture<HttpRequest> requestFuture) {
      return requestFuture
          .thenCompose(
              request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()))
          .thenApply(JavaHttpRestClient::validate);
    }

    private CompletableFuture<HttpRequest.Builder> requestBuilder(
        final URI uri, Map<String, String> headers) {
      final String[] headersAsArray =
          headers.entrySet().stream()
              .flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
              .toArray(String[]::new);
      Builder builder = HttpRequest.newBuilder().uri(uri);
      if (headersAsArray.length > 0) {
        builder = builder.headers(headersAsArray);
      }
      if (requestTimeout != null) {
        builder = builder.timeout(requestTimeout);
      }
      return CompletableFuture.completedFuture(builder);
    }

    @SneakyThrows
    private static String bodyToString(HttpResponse<InputStream> response) {
      try (InputStream body = response.body()) {
        return body == null ? null : new String(body.readAllBytes());
      }
    }
  }
}
