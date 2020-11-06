package com.sinch.sdk.model.conversationapi.optin.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.optin.OptIn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptInResponse {
  @JsonProperty("opt_in")
  private OptIn optIn;

  // ID for the asynchronous request, will be generated id if not set in request
  @JsonProperty("request_id")
  private String requestId;
}
