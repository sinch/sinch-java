package com.sinch.sdk.api.conversationapi.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.exception.ApiException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.Method;

public class ApacheHttpRestClientFactory implements SinchRestClientFactory {
  private final CloseableHttpAsyncClient httpClient;

  public ApacheHttpRestClientFactory(final CloseableHttpAsyncClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public SinchRestClient getClient(
      AuthenticationService authenticationService, ObjectMapper objectMapper) {
    return new ApacheHttpRestClient(authenticationService, httpClient, objectMapper);
  }

  private static class ApacheHttpRestClient implements SinchRestClient {

    private final CloseableHttpAsyncClient client;
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    ApacheHttpRestClient(
        AuthenticationService authenticationService,
        CloseableHttpAsyncClient client,
        ObjectMapper objectMapper) {
      this.authenticationService = authenticationService;
      this.client = client;
      this.objectMapper = objectMapper;
    }

    @Override
    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz) {
      return send(clazz, requestBuilder(uri, Method.GET));
    }

    @Override
    public CompletableFuture<Void> post(URI uri) {
      return send(requestBuilder(uri, Method.POST)
              .thenApply(
                  request -> {
                    request.setBody(new byte[] {}, ContentType.APPLICATION_JSON);
                    return request;
                  }))
          .thenAccept(res -> {});
    }

    @Override
    public <S> CompletableFuture<Void> post(URI uri, S body) {
      return send(requestBuilder(uri, Method.POST)
              .thenApply(
                  request -> {
                    request.setBody(getBytes(body), ContentType.APPLICATION_JSON);
                    return request;
                  }))
          .thenAccept(res -> {});
    }

    @Override
    public <T, S> CompletableFuture<T> post(URI uri, Class<T> clazz, S body) {
      return send(
          clazz,
          requestBuilder(uri, Method.POST)
              .thenApply(
                  request -> {
                    request.setBody(getBytes(body), ContentType.APPLICATION_JSON);
                    return request;
                  }));
    }

    @Override
    public <T, S> CompletableFuture<T> patch(URI uri, Class<T> clazz, S body) {
      return send(
          clazz,
          requestBuilder(uri, Method.PATCH)
              .thenApply(
                  request -> {
                    request.setBody(getBytes(body), ContentType.APPLICATION_JSON);
                    return request;
                  }));
    }

    @Override
    public CompletableFuture<Void> delete(URI uri) {
      return send(requestBuilder(uri, Method.DELETE)).thenAccept(response -> {});
    }

    private CompletableFuture<SimpleHttpRequest> requestBuilder(final URI uri, Method method) {
      return authenticationService
          .getHeaderValue()
          .thenApply(
              authHeaderValue -> {
                SimpleHttpRequest request = SimpleHttpRequests.create(method, uri);
                request.addHeader(AuthenticationService.HEADER_KEY_AUTH, authHeaderValue);
                return request;
              });
    }

    private <T> CompletableFuture<T> send(
        final Class<T> clazz, final CompletableFuture<SimpleHttpRequest> request) {
      return send(request)
          .thenApply(
              response -> {
                try {
                  return response.getBody() != null
                      ? objectMapper.readValue(response.getBody().getBodyBytes(), clazz)
                      : null;
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
    }

    private CompletableFuture<SimpleHttpResponse> send(
        final CompletableFuture<SimpleHttpRequest> requestFuture) {
      CompletableFuture<SimpleHttpResponse> responseFuture = new CompletableFuture<>();
      requestFuture.thenAccept(
          request ->
              client.execute(
                  request,
                  new FutureCallback<>() {
                    @Override
                    public void completed(SimpleHttpResponse response) {
                      try {
                        SimpleHttpResponse validatedResponse = validate(request, response);
                        responseFuture.complete(validatedResponse);
                      } catch (ApiException exception) {
                        responseFuture.completeExceptionally(exception);
                      }
                    }

                    @Override
                    public void failed(Exception exception) {
                      responseFuture.completeExceptionally(exception);
                    }

                    @Override
                    public void cancelled() {
                      responseFuture.cancel(true);
                    }
                  }));

      return responseFuture;
    }

    private static SimpleHttpResponse validate(
        final SimpleHttpRequest request, final SimpleHttpResponse response) {
      final int statusCode = response.getCode();
      if (statusCode / 100 != 2) {
        throw new ApiException(
            statusCode,
            "Call to " + request.getRequestUri() + " received non-success response",
            toHttpHeaders(response.getHeaders()),
            bodyToString(response));
      }
      return response;
    }

    private static String bodyToString(SimpleHttpResponse response) {
      return response.getBody() == null ? null : new String(response.getBody().getBodyBytes());
    }

    @SneakyThrows
    private byte[] getBytes(final Object body) {
      return objectMapper.writeValueAsBytes(body);
    }

    private static HttpHeaders toHttpHeaders(Header[] headers) {
      final Map<String, List<String>> headersMap =
          Arrays.stream(headers)
              .collect(
                  Collectors.groupingBy(
                      Header::getName, Collectors.mapping(Header::getValue, Collectors.toList())));
      return HttpHeaders.of(headersMap, (s1, s2) -> true);
    }
  }
}
