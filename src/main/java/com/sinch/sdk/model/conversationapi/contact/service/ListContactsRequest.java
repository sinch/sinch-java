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
public class ListContactsRequest {
  /* Optional. The maximum number of contacts to fetch. The default is 10
   * and the maximum is 20 */
  @JsonProperty("page_size")
  private int pageSize;

  // Optional. Next page token previously returned if any.
  @JsonProperty("page_token")
  private String pageToken;
}
