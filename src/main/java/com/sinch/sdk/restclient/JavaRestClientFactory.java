package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;

public class JavaRestClientFactory implements SinchRestClientFactory {

  public JavaRestClientFactory(Object httpClient) {
    throw new RuntimeException("Not supported for JDK 8");
  }

  @Override
  public SinchRestClient getClient(Duration requestTimeout, ObjectMapper objectMapper) {
    throw new RuntimeException("Not supported for JDK 8");
  }
}
