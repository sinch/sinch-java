package com.sinch.sdk.model.conversationapi.conversation.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMessagesRequest {
  /* Optional. Resource name (id) of the conversation.
   * One of conversation_id or contact_id needs to be present. */
  @JsonProperty("conversation_id")
  private String conversationId;

  /* Optional. Resource name (id) of the contact.
   * One of conversation_id or contact_id needs to be present */
  @JsonProperty("contact_id")
  private String contactId;

  /* Optional. Maximum number of messages to fetch. Defaults to 10
   * and the maximum is 20 */
  @JsonProperty("page_size")
  @Size(max = 20)
  private int pageSize;

  // Optional. Next page token previously returned if any.
  @JsonProperty("page_token")
  private String pageToken;

  private ConversationMessagesView view;

  public enum ConversationMessagesView {
    WITH_METADATA,
    WITHOUT_METADATA
  }
}
