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
public class ListConversationsRequest {
  /* Optional. The ID of the app involved in the conversations.
   * At least one of app_id and contact_id needs to be present. */
  @JsonProperty("app_id")
  private String appId;

  /* Optional. The ID of the contact involved in the conversations.
   * At least one of app_id and contact_id needs to be present. */
  @JsonProperty("contact_id")
  private String contactId;

  // Required. True if only active conversations should be listed.
  @JsonProperty("only_active")
  private Boolean onlyActive;

  /* Optional. The maximum number of conversations to fetch. Defaults to 10
   * and the maximum is 20 */
  @JsonProperty("page_size")
  @Size(max = 20)
  private int pageSize;

  // Optional. Next page token previously returned if any.
  @JsonProperty("page_token")
  private String pageToken;
}
