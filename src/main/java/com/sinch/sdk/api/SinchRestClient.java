package com.sinch.sdk.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.exception.ApiException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.SneakyThrows;

public class SinchRestClient {

  private static final String PATCH = "PATCH";

  private final AuthenticationService authenticationService;
  private final HttpClient client;
  private final ObjectMapper objectMapper;

  public SinchRestClient(
      final AuthenticationService authenticationService,
      final HttpClient httpClient,
      final ObjectMapper objectMapper) {
    this.authenticationService = authenticationService;
    this.client = httpClient;
    this.objectMapper = objectMapper;
  }

  public <T> T get(final URI uri, final Class<T> clazz) {
    return getAsync(uri, clazz).join();
  }

  public <T> CompletableFuture<T> getAsync(final URI uri, final Class<T> clazz) {
    return sendAsync(clazz, requestBuilder(uri).GET().build());
  }

  public <T, S> T post(final URI uri, final Class<T> clazz, final S body) {
    return postAsync(uri, clazz, body).join();
  }

  public CompletableFuture<HttpResponse<InputStream>> post(final URI uri) {
    return sendAsync(requestBuilder(uri).POST(HttpRequest.BodyPublishers.noBody()).build());
  }

  @SneakyThrows
  public <T, S> CompletableFuture<T> postAsync(final URI uri, final Class<T> clazz, final S body) {
    return sendAsync(
        clazz,
        requestBuilder(uri)
            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(body)))
            .build());
  }

  public <T, S> T patch(final URI uri, final Class<T> clazz, final S body) {
    return patchAsync(uri, clazz, body).join();
  }

  @SneakyThrows
  public <S> CompletableFuture<HttpResponse<InputStream>> patch(final URI uri, final S body) {
    return sendAsync(
        requestBuilder(uri)
            .method(
                PATCH, HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(body)))
            .build());
  }

  @SneakyThrows
  public <T, S> CompletableFuture<T> patchAsync(final URI uri, final Class<T> clazz, final S body) {
    return sendAsync(
        clazz,
        requestBuilder(uri)
            .method(
                PATCH, HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(body)))
            .build());
  }

  public CompletableFuture<Void> delete(final URI uri) {
    return sendAsync(requestBuilder(uri).DELETE().build()).thenAccept(res -> {});
  }

  public <T> CompletableFuture<T> sendAsync(final Class<T> clazz, final HttpRequest request) {
    return sendAsync(request)
        // TODO: Error handling? 401 -> authenticationService.reload()
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

  public CompletableFuture<HttpResponse<InputStream>> sendAsync(final HttpRequest request) {
    return client.sendAsync(request, BodyHandlers.ofInputStream()).thenApply(this::validate);
  }

  private HttpResponse<InputStream> validate(final HttpResponse<InputStream> response) {
    final int statusCode = response.statusCode();
    if (statusCode / 100 != 2) {
      final String responseBody;
      try {
        responseBody = response.body() == null ? null : new String(response.body().readAllBytes());
      } catch (IOException e) {
        throw new CompletionException(e);
      }
      throw new CompletionException(
          new ApiException(
              statusCode,
              "Call to " + response.uri() + " received non-success response",
              response.headers(),
              responseBody));
    }
    return response;
  }

  private HttpRequest.Builder requestBuilder(final URI uri) {
    return HttpRequest.newBuilder()
        .uri(uri)
        .header(AuthenticationService.HEADER_KEY_AUTH, authenticationService.getHeaderValue());
  }
}
