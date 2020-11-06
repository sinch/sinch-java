package com.sinch.sdk.model.conversationapi.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Reason;
import com.sinch.sdk.model.conversationapi.common.enums.CapabilityStatus;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Represents an explicit Capability result for specific channel.
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityNotification {

  // Optional. The contact for the channel identities.
  @JsonProperty("contact_id")
  private String contactId;

  // Optional. The channel identity e.g., a phone number for SMS, WhatsApp and Viber Business.
  private String identity;

  // Optional. The channel for which the capability lookup was done towards.
  private ConversationChannel channel;

  // Required. Status indicating the recipient's capability on the channel.
  @JsonProperty("capability_status")
  private CapabilityStatus capabilityStatus;

  // Optional. A reason shall be present if the capability check failed.
  private Reason reason;

  // Optional. List of channel-specific capabilities reported by the channel.
  @JsonProperty("channel_capabilities")
  private List<String> channelCapabilities;

  // Output only. ID generated when submitting the capability request.
  @JsonProperty("request_id")
  private String requestId;
}
