package com.sinch.sdk.api.conversationapi.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.exception.ApiException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

public class OkHttpRestClientFactory implements SinchRestClientFactory {

  private final OkHttpClient httpClient;

  public OkHttpRestClientFactory(final OkHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public SinchRestClient getClient(
      final AuthenticationService authenticationService, final ObjectMapper objectMapper) {
    return new OkHttpRestClient(authenticationService, httpClient, objectMapper);
  }

  private static class OkHttpRestClient implements SinchRestClient {

    private final OkHttpClient client;
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    OkHttpRestClient(
        AuthenticationService authenticationService,
        OkHttpClient client,
        ObjectMapper objectMapper) {
      this.authenticationService = authenticationService;
      this.client = client;
      this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz) {
      return send(clazz, requestBuilder(uri.toURL()).thenApply(Request.Builder::build));
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Void> post(URI uri) {
      return send(requestBuilder(uri.toURL())
              .thenApply(builder -> builder.post(RequestBody.create(new byte[] {})).build()))
          .thenAccept(res -> {});
    }

    @SneakyThrows
    @Override
    public <S> CompletableFuture<Void> post(URI uri, S body) {
      return send(requestBuilder(uri.toURL())
              .thenApply(builder -> builder.post(RequestBody.create(getBytes(body))).build()))
          .thenAccept(res -> {});
    }

    @SneakyThrows
    @Override
    public <T, S> CompletableFuture<T> post(URI uri, Class<T> clazz, S body) {
      return send(
          clazz,
          requestBuilder(uri.toURL())
              .thenApply(builder -> builder.post(RequestBody.create(getBytes(body))).build()));
    }

    @SneakyThrows
    @Override
    public <T, S> CompletableFuture<T> patch(URI uri, Class<T> clazz, S body) {
      return send(
          clazz,
          requestBuilder(uri.toURL())
              .thenApply(builder -> builder.patch(RequestBody.create(getBytes(body))).build()));
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Void> delete(URI uri) {
      return send(requestBuilder(uri.toURL()).thenApply(builder -> builder.delete().build()))
          .thenAccept(Response::close);
    }

    private CompletableFuture<Request.Builder> requestBuilder(final URL url) {
      return authenticationService
          .getHeaderValue()
          .thenApply(
              authHeaderValue ->
                  new Request.Builder()
                      .url(url)
                      .addHeader(AuthenticationService.HEADER_KEY_AUTH, authHeaderValue));
    }

    private <T> CompletableFuture<T> send(
        final Class<T> clazz, final CompletableFuture<Request> request) {
      return send(request)
          .thenApply(
              response -> {
                try (ResponseBody body = response.body()) {
                  return body != null ? objectMapper.readValue(body.byteStream(), clazz) : null;
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
    }

    private CompletableFuture<Response> send(final CompletableFuture<Request> requestFuture) {
      CompletableFuture<Response> responseFuture = new CompletableFuture<>();
      requestFuture.thenAccept(
          request ->
              client
                  .newCall(request)
                  .enqueue(
                      new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                          responseFuture.completeExceptionally(exception);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) {
                          responseFuture.complete(response);
                        }
                      }));

      return responseFuture.thenApply(OkHttpRestClient::validate);
    }

    private static Response validate(final Response response) {
      final int statusCode = response.code();
      if (statusCode / 100 != 2) {
        throw new ApiException(
            statusCode,
            "Call to " + response.request().url().uri() + " received non-success response",
            HttpHeaders.of(response.headers().toMultimap(), (s1, s2) -> true),
            bodyToString(response));
      }
      return response;
    }

    @SneakyThrows
    private static String bodyToString(Response response) {
      try (ResponseBody body = response.body()) {
        return body == null ? null : new String(body.bytes());
      }
    }

    @SneakyThrows
    private byte[] getBytes(final Object body) {
      return objectMapper.writeValueAsBytes(body);
    }
  }
}
