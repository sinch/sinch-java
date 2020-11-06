package com.sinch.sdk.model.conversationapi.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Reason;
import com.sinch.sdk.model.conversationapi.common.enums.Status;
import com.sinch.sdk.model.conversationapi.contact.Contact.ChannelIdentity;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Message Delivery Report
 *
 * A delivery receipt for message sent from an app. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDeliveryReport {
  // Required. The ID of the message.
  @JsonProperty("message_id")
  private String messageId;

  // Required. The conversation ID this message belongs to.
  @JsonProperty("conversation_id")
  private String conversationId;

  // Required. The delivery status.
  private Status status;

  // Required. The channel and contact channel identity of the message.
  @JsonProperty("channel_identity")
  private ChannelIdentity channelIdentity;

  // Required. The ID of the contact.
  @JsonProperty("contact_id")
  private String contactId;

  // Optional. A reason will be present if the status is FAILED or SWITCHING_CHANNEL.
  private Reason reason;

  /* Optional. Eventual metadata specified when sending the message.
   * Up to 1024 characters long. */
  @Size(max = 1024)
  private String metadata;
}
