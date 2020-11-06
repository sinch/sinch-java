package com.sinch.sdk.model.conversationapi.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Unsupported Channel Callback
 *
 * A callback received on a channel which is not
 * natively supported by Conversation API. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnsupportedCallback {
  // Output only. The originating channel of the unsupported callback.
  private ConversationChannel channel;

  // Output only. The unsupported callback received by the Conversation API.
  private String payload;
}
