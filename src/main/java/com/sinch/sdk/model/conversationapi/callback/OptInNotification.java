package com.sinch.sdk.model.conversationapi.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.common.enums.OptInStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptInNotification {
  // Required. The contact for the channel identities.
  @JsonProperty("contact_id")
  private String contactId;

  // Required. The channel of the opt-in.
  private ConversationChannel channel;

  // Required. The channel identity e.g., a phone number for SMS, WhatsApp and Viber Business.
  private String identity;

  // Required. Status of the opt-in registration.
  private OptInStatus status;

  // Optional. It is set in case of errors.
  @JsonProperty("error_details")
  private OptInErrorDetails errorDetails;

  // Output only. ID generated when submitting the opt in request.
  @JsonProperty("request_id")
  private String requestId;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OptInErrorDetails {
    // Human-readable error description.
    private String description;
  }
}
