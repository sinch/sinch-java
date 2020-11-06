package com.sinch.sdk.model.conversationapi.transcoding.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.message.AppMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscodeMessageRequest {
  // Required. The ID of the app used to transcode the message.
  @JsonProperty("app_id")
  private String appId;

  // Required. The message that should be transcoded.
  @JsonProperty("app_message")
  private AppMessage appMessage;

  // Optional.
  private String from;

  // Optional.
  private String to;

  // Required. The list of channels for which the message shall be transcoded to.
  private List<ConversationChannel> channels;
}
