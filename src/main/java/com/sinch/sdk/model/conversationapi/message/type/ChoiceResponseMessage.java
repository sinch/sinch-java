package com.sinch.sdk.model.conversationapi.message.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Choice Response Message
 *
 * Represents a response to a choice message. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceResponseMessage {
  // Required. The message id containing the choice.
  @JsonProperty("message_id")
  private String messageId;

  // Required. The postback_data defined in the selected choice.
  @JsonProperty("postback_data")
  private String postbackData;
}
