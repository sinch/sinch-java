package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.exception.ConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.util.Timeout;

public class ApacheHttpRestClientFactory implements SinchRestClientFactory {
  private final CloseableHttpAsyncClient httpClient;

  public ApacheHttpRestClientFactory(final CloseableHttpAsyncClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public SinchRestClient getClient(final Duration requestTimeout, final ObjectMapper objectMapper) {
    return new ApacheHttpRestClient(httpClient, requestTimeout, objectMapper);
  }

  private static class ApacheHttpRestClient implements SinchRestClient {

    private final CloseableHttpAsyncClient client;
    private final RequestConfig requestConfig;
    private final ObjectMapper objectMapper;

    ApacheHttpRestClient(
        CloseableHttpAsyncClient client, Duration requestTimeout, ObjectMapper objectMapper) {
      this.client = client;
      this.requestConfig = getRequestConfig(requestTimeout);
      this.objectMapper = objectMapper;
    }

    private RequestConfig getRequestConfig(Duration requestTimeout) {
      if (requestTimeout == null) {
        return RequestConfig.DEFAULT;
      }
      return RequestConfig.custom()
          .setResponseTimeout(Timeout.ofSeconds(requestTimeout.getSeconds()))
          .build();
    }

    @Override
    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz, Map<String, String> headers) {
      return send(clazz, requestBuilder(uri, Method.GET, headers));
    }

    @Override
    public CompletableFuture<Void> post(URI uri, Map<String, String> headers) {
      return send(requestBuilder(uri, Method.POST, headers)
              .thenApply(
                  request -> {
                    request.setBody(new byte[] {}, ContentType.APPLICATION_JSON);
                    return request;
                  }))
          .thenAccept(res -> {});
    }

    @Override
    public <S> CompletableFuture<Void> post(URI uri, S body, Map<String, String> headers) {
      return send(requestBuilder(uri, Method.POST, headers)
              .thenApply(
                  request -> {
                    request.setBody(getBytes(objectMapper, body), ContentType.APPLICATION_JSON);
                    return request;
                  }))
          .thenAccept(res -> {});
    }

    @Override
    public <T, S> CompletableFuture<T> post(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return send(
          clazz,
          requestBuilder(uri, Method.POST, headers)
              .thenApply(
                  request -> {
                    request.setBody(getBytes(objectMapper, body), ContentType.APPLICATION_JSON);
                    return request;
                  }));
    }

    @Override
    public <T, S> CompletableFuture<T> patch(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return send(
          clazz,
          requestBuilder(uri, Method.PATCH, headers)
              .thenApply(
                  request -> {
                    request.setBody(getBytes(objectMapper, body), ContentType.APPLICATION_JSON);
                    return request;
                  }));
    }

    @Override
    public CompletableFuture<Void> delete(URI uri, Map<String, String> headers) {
      return send(requestBuilder(uri, Method.DELETE, headers)).thenAccept(response -> {});
    }

    private CompletableFuture<SimpleHttpRequest> requestBuilder(
        final URI uri, Method method, Map<String, String> headers) {
      SimpleHttpRequest request = SimpleHttpRequests.create(method, uri);
      request.setConfig(requestConfig);
      headers.forEach(request::addHeader);
      return CompletableFuture.completedFuture(request);
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
                      } catch (ConfigurationException | ApiException exception) {
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
      if (statusCode == 401) {
        throw new ConfigurationException(
            "Invalid credentials, verify the keyId and keySecret", bodyToString(response));
      }
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
