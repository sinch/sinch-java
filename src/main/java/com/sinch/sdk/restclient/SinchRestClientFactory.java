package com.sinch.sdk.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;

public interface SinchRestClientFactory {
  SinchRestClient getClient(final Duration requestTimeout, final ObjectMapper objectMapper);
}
