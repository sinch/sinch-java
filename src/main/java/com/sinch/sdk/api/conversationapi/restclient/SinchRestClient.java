package com.sinch.sdk.api.conversationapi.restclient;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public interface SinchRestClient {

  <T> CompletableFuture<T> get(final URI uri, final Class<T> clazz);

  CompletableFuture<Void> post(final URI uri);

  <S> CompletableFuture<Void> post(final URI uri, final S body);

  <T, S> CompletableFuture<T> post(final URI uri, final Class<T> clazz, final S body);

  <T, S> CompletableFuture<T> patch(final URI uri, final Class<T> clazz, final S body);

  CompletableFuture<Void> delete(final URI uri);
}
