package com.sinch.sdk.model.conversationapi.transcoding.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscodeMessageResponse {
  /* Output only. The transcoded message for the different channels.
   * The keys in the map correspond to channel names, as defined by the type
   * ConversationChannel. */
  @JsonProperty("transcoded_message")
  private Map<String, String> transcodedMessage;
}
