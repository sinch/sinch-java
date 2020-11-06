package com.sinch.sdk.model.conversationapi.capability.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryCapabilityRequest {

  // Required. The ID of the app to use for capability lookup.
  @JsonProperty("app_id")
  private String appId;

  // Required. The recipient to do the lookup for.
  private Recipient recipient;

  /*
   * ID for the asynchronous request, will be generated if not set.
   * Currently this field is not used for idempotency but it will be added in v1
   */
  @JsonProperty("request_id")
  private String requestId;
}
