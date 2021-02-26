package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.restclient.ResponseValidator.ResponseMetadata;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.jetbrains.annotations.NotNull;

public class OkHttpRestClientFactory implements SinchRestClientFactory {

  private final OkHttpClient httpClient;

  public OkHttpRestClientFactory(final OkHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public SinchRestClient getClient(final Duration requestTimeout, final ObjectMapper objectMapper) {
    return new OkHttpRestClient(httpClient, requestTimeout, objectMapper);
  }

  @Slf4j
  private static class OkHttpRestClient implements SinchRestClient {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final ResponseValidator responseValidator;

    OkHttpRestClient(OkHttpClient client, Duration requestTimeout, ObjectMapper objectMapper) {
      this.client =
          requestTimeout != null ? client.newBuilder().readTimeout(requestTimeout).build() : client;
      log.info("Uses {} request timeout", requestTimeout != null ? requestTimeout : "default");
      this.objectMapper = objectMapper;
      this.responseValidator = new ResponseValidator();
    }

    @SneakyThrows
    @Override
    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz, Map<String, String> headers) {
      return send(clazz, requestBuilder(uri.toURL(), headers).thenApply(Request.Builder::build));
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Void> post(URI uri, Map<String, String> headers) {
      return send(requestBuilder(uri.toURL(), headers)
              .thenApply(builder -> builder.post(RequestBody.create(new byte[] {})).build()))
          .thenAccept(responseBody -> {});
    }

    @SneakyThrows
    @Override
    public <S> CompletableFuture<Void> post(URI uri, S body, Map<String, String> headers) {
      return send(requestBuilder(uri.toURL(), headers)
              .thenApply(
                  builder ->
                      builder.post(RequestBody.create(getBytes(objectMapper, body))).build()))
          .thenAccept(responseBody -> {});
    }

    @SneakyThrows
    @Override
    public <T, S> CompletableFuture<T> post(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return send(
          clazz,
          requestBuilder(uri.toURL(), headers)
              .thenApply(
                  builder ->
                      builder.post(RequestBody.create(getBytes(objectMapper, body))).build()));
    }

    @SneakyThrows
    @Override
    public <T, S> CompletableFuture<T> patch(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return send(
          clazz,
          requestBuilder(uri.toURL(), headers)
              .thenApply(
                  builder ->
                      builder.patch(RequestBody.create(getBytes(objectMapper, body))).build()));
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Void> delete(URI uri, Map<String, String> headers) {
      return send(requestBuilder(uri.toURL(), headers)
              .thenApply(builder -> builder.delete().build()))
          .thenAccept(responseBody -> {});
    }

    private CompletableFuture<Request.Builder> requestBuilder(
        final URL url, Map<String, String> headers) {
      final Builder builder = new Builder().url(url);
      headers.forEach(builder::addHeader);
      return CompletableFuture.completedFuture(builder);
    }

    private <T> CompletableFuture<T> send(
        final Class<T> clazz, final CompletableFuture<Request> request) {
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

    private CompletableFuture<byte[]> send(final CompletableFuture<Request> requestFuture) {
      CompletableFuture<Response> responseFuture = new CompletableFuture<>();
      requestFuture.thenAccept(
          request -> {
            log.debug("Send {} request to: {}", request.method(), request.url().uri());
            client
                .newCall(request)
                .enqueue(
                    new Callback() {
                      @Override
                      public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                        log.error("Received an exception from {}", request.url().uri(), exception);
                        responseFuture.completeExceptionally(exception);
                      }

                      @Override
                      public void onResponse(@NotNull Call call, @NotNull Response response) {
                        responseFuture.complete(response);
                      }
                    });
          });

      return responseFuture
          .thenApply(
              response ->
                  new ResponseMetadata(
                      response.code(),
                      response.request().url().uri(),
                      getResponseBodyBytes(response),
                      new HttpHeaders(response.headers().toMultimap())))
          .thenApply(responseMetadata -> responseValidator.validate(responseMetadata, log));
    }

    @SneakyThrows
    private static byte[] getResponseBodyBytes(Response response) {
      try (ResponseBody body = response.body()) {
        return body == null ? new byte[] {} : body.bytes();
      }
    }
  }
}
