package com.sinch.sdk.model.conversationapi.contact.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.contact.Contact;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListContactsResponse {
  /* Token that should be included in the next list contacts request to
   * fetch the next page. */
  @JsonProperty("next_page_token")
  String nextPageToken;
  // List of contacts belonging to the specified project.
  private List<Contact> contacts;
}
