package com.sinch.sdk.model.conversationapi.capability.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Represents an explicit Capability registration
 *
 * An CapabilityResponse contains the identity of the recipient for which
 * will be perform a capability lookup.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryCapabilityResponse {
  // Required. The ID of the app to use for capability lookup.
  @JsonProperty("app_id")
  private String appId;

  // Required. The recipient for which a capability lookup was triggered for.
  private Recipient recipient;

  // ID for the asynchronous request, will be generated id if not set in request
  @JsonProperty("request_id")
  private String requestId;
}
