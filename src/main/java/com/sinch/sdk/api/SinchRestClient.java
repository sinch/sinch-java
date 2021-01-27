package com.sinch.sdk.api;

import static java.util.concurrent.CompletableFuture.supplyAsync;

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

  public <T> CompletableFuture<T> get(final URI uri, final Class<T> clazz) {
    return send(clazz, supplyAsync(() -> requestBuilder(uri).GET().build()));
  }

  public CompletableFuture<Void> post(final URI uri) {
    return send(supplyAsync(
            () -> requestBuilder(uri).POST(HttpRequest.BodyPublishers.noBody()).build()))
        .thenAccept(res -> {});
  }

  @SneakyThrows
  public <S> CompletableFuture<Void> post(final URI uri, final S body) {
    return send(supplyAsync(
            () ->
                requestBuilder(uri)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(getBytes(body)))
                    .build()))
        .thenAccept(res -> {});
  }

  @SneakyThrows
  public <T, S> CompletableFuture<T> post(final URI uri, final Class<T> clazz, final S body) {
    return send(
        clazz,
        supplyAsync(
            () ->
                requestBuilder(uri)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(getBytes(body)))
                    .build()));
  }

  @SneakyThrows
  public <T, S> CompletableFuture<T> patch(final URI uri, final Class<T> clazz, final S body) {
    return send(
        clazz,
        supplyAsync(
            () ->
                requestBuilder(uri)
                    .method(PATCH, HttpRequest.BodyPublishers.ofByteArray(getBytes(body)))
                    .build()));
  }

  public CompletableFuture<Void> delete(final URI uri) {
    return send(supplyAsync(() -> requestBuilder(uri).DELETE().build())).thenAccept(res -> {});
  }

  public <T> CompletableFuture<T> send(
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

  public CompletableFuture<HttpResponse<InputStream>> send(
      final CompletableFuture<HttpRequest> requestFuture) {
    return requestFuture
        .thenCompose(request -> client.sendAsync(request, BodyHandlers.ofInputStream()))
        .thenApply(SinchRestClient::validate);
  }

  public static HttpResponse<InputStream> validate(final HttpResponse<InputStream> response) {
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

  @SneakyThrows
  private byte[] getBytes(final Object body) {
    return objectMapper.writeValueAsBytes(body);
  }
}
