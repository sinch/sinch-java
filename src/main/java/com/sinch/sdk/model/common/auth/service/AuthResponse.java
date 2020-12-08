package com.sinch.sdk.model.common.auth.service;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AuthResponse {
  String accessToken;
  long expiresIn;
  String scope;
  String tokenType;
}
