package com.sinch.sdk.model.conversationapi.message.type.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Call Message
 *
 * Message for triggering a call. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallMessage {
  /* Required. Title shown close to the phone number.
   * The title is clickable in some cases. */
  private String title;

  // Required. Phone number in E.164 with leading +.
  @JsonProperty("phone_number")
  private String phoneNumber;
}
