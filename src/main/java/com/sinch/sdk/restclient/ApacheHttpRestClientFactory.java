package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.restclient.ResponseValidator.ResponseMetadata;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

  @Slf4j
  private static class ApacheHttpRestClient implements SinchRestClient {

    private final CloseableHttpAsyncClient client;
    private final RequestConfig requestConfig;
    private final ObjectMapper objectMapper;
    private final ResponseValidator responseValidator;

    ApacheHttpRestClient(
        CloseableHttpAsyncClient client, Duration requestTimeout, ObjectMapper objectMapper) {
      this.client = client;
      this.requestConfig = getRequestConfig(requestTimeout);
      this.objectMapper = objectMapper;
      this.responseValidator = new ResponseValidator();
    }

    private RequestConfig getRequestConfig(Duration requestTimeout) {
      if (requestTimeout == null) {
        log.info("Uses default request config");
        return RequestConfig.DEFAULT;
      }
      log.info("Uses request config with custom timeout: {}", requestTimeout);
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
          .thenAccept(responseBody -> {});
    }

    @Override
    public <S> CompletableFuture<Void> post(URI uri, S body, Map<String, String> headers) {
      return send(requestBuilder(uri, Method.POST, headers)
              .thenApply(
                  request -> {
                    request.setBody(getBytes(objectMapper, body), ContentType.APPLICATION_JSON);
                    return request;
                  }))
          .thenAccept(responseBody -> {});
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
      return send(requestBuilder(uri, Method.DELETE, headers)).thenAccept(responseBody -> {});
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
              responseBody -> {
                try {
                  return responseBody != null ? objectMapper.readValue(responseBody, clazz) : null;
                } catch (IOException e) {
                  log.error("Exception occurred while trying to read response as {}", clazz);
                  throw new RuntimeException(e);
                }
              });
    }

    private CompletableFuture<byte[]> send(
        final CompletableFuture<SimpleHttpRequest> requestFuture) {
      CompletableFuture<byte[]> responseFuture = new CompletableFuture<>();
      requestFuture.thenAccept(
          request -> {
            log.debug("Send {} request to: {}", request.getMethod(), requestURI(request));
            client.execute(
                request,
                new FutureCallback<>() {
                  @Override
                  public void completed(SimpleHttpResponse response) {
                    try {
                      final ResponseMetadata responseMetadata =
                          new ResponseMetadata(
                              response.getCode(),
                              requestURI(request),
                              response.getBodyBytes(),
                              toHttpHeaders(response.getHeaders()));
                      byte[] validatedResponse = responseValidator.validate(responseMetadata, log);
                      responseFuture.complete(validatedResponse);
                    } catch (ConfigurationException | ApiException exception) {
                      responseFuture.completeExceptionally(exception);
                    }
                  }

                  @Override
                  public void failed(Exception exception) {
                    log.error("Received an exception from {}", request.getRequestUri(), exception);
                    responseFuture.completeExceptionally(exception);
                  }

                  @Override
                  public void cancelled() {
                    responseFuture.cancel(true);
                  }
                });
          });

      return responseFuture;
    }

    @SneakyThrows
    private static URI requestURI(SimpleHttpRequest request) {
      return request.getUri();
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
