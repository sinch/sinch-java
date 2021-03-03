package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;

public interface SinchRestClient {

  default <T> CompletableFuture<T> get(final URI uri, final Class<T> clazz) {
    return get(uri, clazz, Collections.emptyMap());
  }

  <T> CompletableFuture<T> get(final URI uri, final Class<T> clazz, Map<String, String> headers);

  default CompletableFuture<Void> post(final URI uri) {
    return post(uri, Collections.emptyMap());
  }

  CompletableFuture<Void> post(final URI uri, Map<String, String> headers);

  default <S> CompletableFuture<Void> post(final URI uri, final S body) {
    return post(uri, body, Collections.emptyMap());
  }

  <S> CompletableFuture<Void> post(final URI uri, final S body, Map<String, String> headers);

  default <T, S> CompletableFuture<T> post(final URI uri, final Class<T> clazz, final S body) {
    return post(uri, clazz, body, Collections.emptyMap());
  }

  <T, S> CompletableFuture<T> post(
      final URI uri, final Class<T> clazz, final S body, Map<String, String> headers);

  default <T, S> CompletableFuture<T> patch(final URI uri, final Class<T> clazz, final S body) {
    return patch(uri, clazz, body, Collections.emptyMap());
  }

  <T, S> CompletableFuture<T> patch(
      final URI uri, final Class<T> clazz, final S body, Map<String, String> headers);

  default CompletableFuture<Void> delete(final URI uri) {
    return delete(uri, Collections.emptyMap());
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
