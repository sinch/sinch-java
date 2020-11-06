package com.sinch.sdk.model.conversationapi.message.type.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Reply To
 *
 * If the contact message was a response to a previous App message then this field contains information about that. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyTo {
  // Required. The Id of the message that this is a response to
  @JsonProperty("message_id")
  private String messageId;
}
