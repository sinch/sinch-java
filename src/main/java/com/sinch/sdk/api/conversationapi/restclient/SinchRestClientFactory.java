package com.sinch.sdk.api.conversationapi.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.authentication.AuthenticationService;
import java.net.http.HttpClient;
import okhttp3.OkHttpClient;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;

public interface SinchRestClientFactory {
  SinchRestClient getClient(
      final AuthenticationService authenticationService, final ObjectMapper objectMapper);

  static SinchRestClientFactory create(HttpClient httpClient) {
    return new JavaRestClientFactory(httpClient);
  }

  static SinchRestClientFactory create(OkHttpClient httpClient) {
    return new OkHttpRestClientFactory(httpClient);
  }

  static SinchRestClientFactory create(CloseableHttpAsyncClient httpClient) {
    return new ApacheHttpRestClientFactory(httpClient);
  }
}
