package com.sinch.sdk.model.conversationapi.message.type.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Url Message
 *
 * A generic URL message. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlMessage {
  /* Required. The title shown close to the URL.
   * The title will be clickable in some cases. */
  private String title;

  // Required. The url to show.
  private String url;
}
