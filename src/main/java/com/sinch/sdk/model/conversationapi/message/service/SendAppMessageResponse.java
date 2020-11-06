package com.sinch.sdk.model.conversationapi.message.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendAppMessageResponse {
  // Output only. Message id.
  @JsonProperty("message_id")
  private String messageId;

  /* Output only. Timestamp corresponding to when the Conversation API
   * accepted the message for delivery to the referenced contact. */
  @JsonProperty("accepted_time")
  private String acceptedTime;
}
