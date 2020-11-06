package com.sinch.sdk.client.auth.module;

import com.sinch.sdk.client.auth.AuthMode;
import java.util.Base64;

public class AuthenticationModule {

  private static final String BASIC_AUTH_TEMPLATE = "%s:%s";
  private String authHeader;

  public String getAuthorizationHeader() {
    if (authHeader == null
        || authHeader.isBlank()
        || authHeader == String.format(AuthMode.BASIC_AUTH.getTemplate(), "")
        || authHeader == String.format(AuthMode.BEARER_TOKEN.getTemplate(), ""))
      ; // TODO auth exception
    return authHeader;
  }

  public void setAuthMode(AuthMode authMode, String clientId, String clientSecret) {
    switch (authMode) {
      case BASIC_AUTH:
        authHeader = getBasicAuthHeader(clientId, clientSecret);
        break;
      case BEARER_TOKEN:
        authHeader = getBearerTokenHeader(clientId, clientSecret);
        break;
    }
  }

  private String getBasicAuthHeader(String clientId, String clientSecret) {
    return String.format(
        AuthMode.BASIC_AUTH.getTemplate(),
        Base64.getEncoder()
            .encodeToString(String.format(BASIC_AUTH_TEMPLATE, clientId, clientSecret).getBytes()));
  }

  private String getBearerTokenHeader(String clientId, String clientSecret) {
    return String.format(AuthMode.BEARER_TOKEN.getTemplate(), getToken(clientId, clientSecret));
  }

  // TODO implement get auth token call
  private String getToken(String clientId, String clientSecret) {
    return "your-token";
  }
}
