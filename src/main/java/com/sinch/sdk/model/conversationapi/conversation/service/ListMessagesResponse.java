package com.sinch.sdk.model.conversationapi.conversation.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListMessagesResponse {
  // Output only. List of messages associated to the referenced conversation.
  private List<ConversationMessage> messages;

  @JsonProperty("next_page_token")
  private String nextPageToken;
}
