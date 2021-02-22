package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.restclient.SinchRestClient;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SinchRestClientProxy implements SinchRestClient {

  private final AuthenticationService authenticationService;
  private final SinchRestClient sinchRestClient;

  SinchRestClientProxy(
      AuthenticationService authenticationService, SinchRestClient sinchRestClient) {
    this.authenticationService = authenticationService;
    this.sinchRestClient = sinchRestClient;
  }

  @Override
  public <T> CompletableFuture<T> get(URI uri, Class<T> clazz, Map<String, String> headers) {
    return allHeaders(headers)
        .thenCompose(allHeaders -> sinchRestClient.get(uri, clazz, allHeaders));
  }

  @Override
  public CompletableFuture<Void> post(URI uri, Map<String, String> headers) {
    return allHeaders(headers).thenCompose(allHeaders -> sinchRestClient.post(uri, allHeaders));
  }

  @Override
  public <S> CompletableFuture<Void> post(URI uri, S body, Map<String, String> headers) {
    return allHeaders(headers)
        .thenCompose(allHeaders -> sinchRestClient.post(uri, body, allHeaders));
  }

  @Override
  public <T, S> CompletableFuture<T> post(
      URI uri, Class<T> clazz, S body, Map<String, String> headers) {
    return allHeaders(headers)
        .thenCompose(allHeaders -> sinchRestClient.post(uri, clazz, body, allHeaders));
  }

  @Override
  public <T, S> CompletableFuture<T> patch(
      URI uri, Class<T> clazz, S body, Map<String, String> headers) {
    return allHeaders(headers)
        .thenCompose(allHeaders -> sinchRestClient.patch(uri, clazz, body, allHeaders));
  }

  @Override
  public CompletableFuture<Void> delete(URI uri, Map<String, String> headers) {
    return allHeaders(headers).thenCompose(allHeaders -> sinchRestClient.delete(uri, allHeaders));
  }

  private CompletableFuture<Map<String, String>> allHeaders(Map<String, String> headers) {
    return authenticationService
        .getHeaderValue()
        .thenApply(authHeader -> mergeHeaders(headers, authHeader));
  }

  private Map<String, String> mergeHeaders(
      Map<String, String> headers, Map<String, String> authHeaders) {
    return Stream.concat(headers.entrySet().stream(), authHeaders.entrySet().stream())
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
