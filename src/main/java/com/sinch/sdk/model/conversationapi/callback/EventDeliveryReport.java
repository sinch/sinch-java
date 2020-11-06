package com.sinch.sdk.model.conversationapi.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Reason;
import com.sinch.sdk.model.conversationapi.common.enums.Status;
import com.sinch.sdk.model.conversationapi.contact.Contact.ChannelIdentity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Event Delivery Report
 *
 * A delivery receipt for event sent from an app. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDeliveryReport {
  // Required. The ID of the event.
  @JsonProperty("event_id")
  private String eventId;

  // Required. The delivery status.
  private Status status;

  // Required. The channel and contact channel identity of the event.
  @JsonProperty("channel_identity")
  private ChannelIdentity channelIdentity;

  // Required. The ID of the contact.
  @JsonProperty("contact_id")
  private String contactId;

  // Optional. A reason will be present if the status is FAILED or SWITCHING_CHANNEL.
  private Reason reason;

  // Optional. Eventual metadata specified when sending the message.
  private String metadata;
}
