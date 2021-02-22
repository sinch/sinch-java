package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;

public interface SinchRestClient {

  default <T> CompletableFuture<T> get(final URI uri, final Class<T> clazz) {
    return get(uri, clazz, Map.of());
  }

  <T> CompletableFuture<T> get(final URI uri, final Class<T> clazz, Map<String, String> headers);

  default CompletableFuture<Void> post(final URI uri) {
    return post(uri, Map.of());
  }

  CompletableFuture<Void> post(final URI uri, Map<String, String> headers);

  default <S> CompletableFuture<Void> post(final URI uri, final S body) {
    return post(uri, body, Map.of());
  }

  <S> CompletableFuture<Void> post(final URI uri, final S body, Map<String, String> headers);

  default <T, S> CompletableFuture<T> post(final URI uri, final Class<T> clazz, final S body) {
    return post(uri, clazz, body, Map.of());
  }

  <T, S> CompletableFuture<T> post(
      final URI uri, final Class<T> clazz, final S body, Map<String, String> headers);

  default <T, S> CompletableFuture<T> patch(final URI uri, final Class<T> clazz, final S body) {
    return patch(uri, clazz, body, Map.of());
  }

  <T, S> CompletableFuture<T> patch(
      final URI uri, final Class<T> clazz, final S body, Map<String, String> headers);

  default CompletableFuture<Void> delete(final URI uri) {
    return delete(uri, Map.of());
  }

  CompletableFuture<Void> delete(final URI uri, Map<String, String> headers);

  @SneakyThrows
  default byte[] getBytes(ObjectMapper objectMapper, final Object body) {
    if (body instanceof String) {
      return ((String) body).getBytes(StandardCharsets.UTF_8);
    }
    return objectMapper.writeValueAsBytes(body);
  }
}
