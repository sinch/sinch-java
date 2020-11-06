package com.sinch.sdk.client.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private long expiresIn;

  private String scope;

  @JsonProperty("token_type")
  private String tokenType;
}
