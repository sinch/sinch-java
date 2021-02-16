package com.sinch.sdk.api.conversationapi.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.authentication.AuthenticationService;

public interface SinchRestClientFactory {
  SinchRestClient getClient(
      final AuthenticationService authenticationService, final ObjectMapper objectMapper);
}
