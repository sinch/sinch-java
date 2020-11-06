package com.sinch.sdk.api.conversationapi.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

public abstract class ConversationApiService {
  private static final String AUTH_HEADER = "Authorization";
  private final ObjectMapper objectMapper =
      new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  protected String serviceUrl;
  protected HttpClient client;
  protected Supplier<String> authorizationHeader;

  public ConversationApiService(String serviceUrl, Supplier<String> authorizationHeader) {
    this.serviceUrl = serviceUrl;
    this.authorizationHeader = authorizationHeader;
    this.client = HttpClient.newHttpClient();
  }

  protected <T> T getRequest(String path, Class<T> clazz) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(serviceUrl.concat(path)))
            .header(AUTH_HEADER, authorizationHeader.get())
            .build();

    return sendRequest(clazz, request);
  }

  protected void postRequestEmptyBody(String path) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .POST(BodyPublishers.noBody())
            .uri(URI.create(serviceUrl.concat(path)))
            .header(AUTH_HEADER, authorizationHeader.get())
            .build();

    client.sendAsync(request, BodyHandlers.ofString()).join();
  }

  protected <T, S> T postRequest(String path, Class<T> clazz, S body) {
    String postBody = "";
    try {
      postBody = objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    HttpRequest request =
        HttpRequest.newBuilder()
            .POST(BodyPublishers.ofString(postBody))
            .uri(URI.create(serviceUrl.concat(path)))
            .header(AUTH_HEADER, authorizationHeader.get())
            .build();

    return sendRequest(clazz, request);
  }

  protected <T, S> T patchRequest(String path, Class<T> clazz, S body) {
    String patchBody = "";
    try {
      patchBody = objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    HttpRequest request =
        HttpRequest.newBuilder()
            .method("PATCH", BodyPublishers.ofString(patchBody))
            .uri(URI.create(serviceUrl.concat(path)))
            .header(AUTH_HEADER, authorizationHeader.get())
            .build();

    return sendRequest(clazz, request);
  }

  protected void deleteRequest(String path) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .DELETE()
            .uri(URI.create(serviceUrl.concat(path)))
            .header(AUTH_HEADER, authorizationHeader.get())
            .build();

    client.sendAsync(request, BodyHandlers.ofString()).join();
  }

  private <T> T sendRequest(Class<T> clazz, HttpRequest request) {
    return client
        .sendAsync(request, BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenApply(
            body -> {
              try {
                System.out.println(body);
                return objectMapper.readValue(body, clazz);
              } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
              }
            })
        .join();
  }

  protected String buildQueryParams(Map<String, String> queryParams) {
    if (queryParams.isEmpty()) {
      return "";
    }
    StringBuilder params = new StringBuilder("?");
    for (Entry entry : queryParams.entrySet()) {
      params.append(entry.getKey()).append("=").append(entry.getValue());
    }
    return params.toString();
  }
}
