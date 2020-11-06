package com.sinch.sdk.model.conversationapi.contact.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeContactRequest {
  // Required. The ID of the contact that should be removed.
  @JsonProperty("source_id")
  private String sourceId;

  // Optional. Merge conversation strategy for future usage
  private ConversationMergeStrategy strategy;

  public enum ConversationMergeStrategy {
    MERGE // merge messages of active conversations into one conversation
  }
}
