package com.sinch.sdk.model.conversationapi.event.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEventResponse {
  // Output only. Event id.
  @JsonProperty("event_id")
  private String eventId;

  // Output only. Accepted timestamp.
  @JsonProperty("accepted_time")
  private String acceptedTime;
}
