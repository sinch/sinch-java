package com.sinch.sdk.model.conversationapi.conversation.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListConversationsResponse {
  // Output only. List of conversations matching the search query.
  private List<Conversation> conversations;

  @JsonProperty("next_page_token")
  private String nextPageToken;

  @JsonProperty("total_size")
  private int totalSize;
}
