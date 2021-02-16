package com.sinch.sdk.api.conversationapi.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.exception.ApiException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;

class JavaRestClientFactory implements SinchRestClientFactory {

  private final HttpClient httpClient;

  public JavaRestClientFactory(final HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public SinchRestClient getClient(
      final AuthenticationService authenticationService, final ObjectMapper objectMapper) {
    return new JavaHttpRestClient(authenticationService, httpClient, objectMapper);
  }

  private static class JavaHttpRestClient implements SinchRestClient {

    private static final String PATCH = "PATCH";

    private final AuthenticationService authenticationService;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    JavaHttpRestClient(
        final AuthenticationService authenticationService,
        final HttpClient client,
        final ObjectMapper objectMapper) {
      this.authenticationService = authenticationService;
      this.client = client;
      this.objectMapper = objectMapper;
    }

    @Override
    public <T> CompletableFuture<T> get(final URI uri, final Class<T> clazz) {
      return send(clazz, requestBuilder(uri).thenApply(builder -> builder.GET().build()));
    }

    @Override
    public CompletableFuture<Void> post(final URI uri) {
      return send(requestBuilder(uri)
              .thenApply(builder -> builder.POST(HttpRequest.BodyPublishers.noBody()).build()))
          .thenAccept(res -> {});
    }

    @SneakyThrows
    @Override
    public <S> CompletableFuture<Void> post(final URI uri, final S body) {
      return send(requestBuilder(uri)
              .thenApply(
                  builder ->
                      builder.POST(HttpRequest.BodyPublishers.ofByteArray(getBytes(body))).build()))
          .thenAccept(res -> {});
    }

    @SneakyThrows
    @Override
    public <T, S> CompletableFuture<T> post(final URI uri, final Class<T> clazz, final S body) {
      return send(
          clazz,
          requestBuilder(uri)
              .thenApply(
                  builder ->
                      builder
                          .POST(HttpRequest.BodyPublishers.ofByteArray(getBytes(body)))
                          .build()));
    }

    @SneakyThrows
    @Override
    public <T, S> CompletableFuture<T> patch(final URI uri, final Class<T> clazz, final S body) {
      return send(
          clazz,
          requestBuilder(uri)
              .thenApply(
                  builder ->
                      builder
                          .method(PATCH, HttpRequest.BodyPublishers.ofByteArray(getBytes(body)))
                          .build()));
    }

    public CompletableFuture<Void> delete(final URI uri) {
      return send(requestBuilder(uri).thenApply(builder -> builder.DELETE().build()))
          .thenAccept(res -> {});
    }

    private static HttpResponse<InputStream> validate(final HttpResponse<InputStream> response) {
      final int statusCode = response.statusCode();
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

    private CompletableFuture<HttpRequest.Builder> requestBuilder(final URI uri) {
      return authenticationService
          .getHeaderValue()
          .thenApply(
              authHeaderValue ->
                  HttpRequest.newBuilder()
                      .uri(uri)
                      .header(AuthenticationService.HEADER_KEY_AUTH, authHeaderValue));
    }

    @SneakyThrows
    private static String bodyToString(HttpResponse<InputStream> response) {
      try (InputStream body = response.body()) {
        return body == null ? null : new String(body.readAllBytes());
      }
    }

    @SneakyThrows
    private byte[] getBytes(final Object body) {
      return objectMapper.writeValueAsBytes(body);
    }
  }
}
