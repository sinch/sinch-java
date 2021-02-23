package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.restclient.ResponseValidator.ResponseMetadata;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

public class JavaRestClientFactory implements SinchRestClientFactory {

  private final HttpClient httpClient;

  public JavaRestClientFactory(final HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public SinchRestClient getClient(final Duration requestTimeout, final ObjectMapper objectMapper) {
    return new JavaHttpRestClient(httpClient, requestTimeout, objectMapper);
  }

  @Slf4j
  private static class JavaHttpRestClient implements SinchRestClient {

    private static final String PATCH = "PATCH";

    private final HttpClient client;
    private final Duration requestTimeout;
    private final ObjectMapper objectMapper;
    private final ResponseValidator responseValidator;

    JavaHttpRestClient(
        final HttpClient client, final Duration requestTimeout, final ObjectMapper objectMapper) {
      this.client = client;
      this.requestTimeout = requestTimeout;
      log.info("Using {} request timeout", requestTimeout != null ? requestTimeout : "default");
      this.objectMapper = objectMapper;
      this.responseValidator = new ResponseValidator();
    }

    @Override
    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz, Map<String, String> headers) {
      return send(clazz, requestBuilder(uri, headers).thenApply(builder -> builder.GET().build()));
    }

    @Override
    public CompletableFuture<Void> post(URI uri, Map<String, String> headers) {
      return send(requestBuilder(uri, headers)
              .thenApply(builder -> builder.POST(HttpRequest.BodyPublishers.noBody()).build()))
          .thenAccept(responseBody -> {});
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
          .thenAccept(responseBody -> {});
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
          .thenAccept(responseBody -> {});
    }

    private <T> CompletableFuture<T> send(
        final Class<T> clazz, final CompletableFuture<HttpRequest> request) {
      return send(request)
          .thenApply(
              body -> {
                try {
                  return objectMapper.readValue(body, clazz);
                } catch (IOException e) {
                  log.error("Exception occurred while trying to read response as {}", clazz);
                  throw new RuntimeException(e);
                }
              });
    }

    private CompletableFuture<byte[]> send(final CompletableFuture<HttpRequest> requestFuture) {
      return requestFuture
          .thenCompose(
              request -> {
                log.debug("Send {} request to: {}", request.method(), request.uri());
                return client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray());
              })
          .whenComplete(JavaHttpRestClient::logExceptions)
          .thenApply(
              httpResponse ->
                  new ResponseMetadata(
                      httpResponse.statusCode(),
                      httpResponse.uri(),
                      httpResponse.body(),
                      httpResponse.headers()))
          .thenApply(responseMetadata -> responseValidator.validate(responseMetadata, log));
    }

    private static void logExceptions(HttpResponse<byte[]> response, Throwable throwable) {
      if (throwable != null) {
        log.error("Received an exception from {}", response.uri(), throwable);
        if (log.isDebugEnabled()) {
          log.debug(
              "Received response from {} with status {} and body: {}",
              response.uri(),
              response.statusCode(),
              BodyMapper.bodyToString(response.body()));
        }
      }
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
  }
}
