package com.sinch.sdk.model.conversationapi.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationDirection;
import com.sinch.sdk.model.conversationapi.contact.Contact.ChannelIdentity;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Conversation Message
 *
 * A message on a particular channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class ConversationMessage {

  // Required. The ID of the message.
  private String id;
  /* The direction defines what is the source and
   * what is the destination of the message. */
  private ConversationDirection direction;
  // The content of the message.
  @JsonProperty("app_message")
  private AppMessage appMessage;
  // The content of the message.
  @JsonProperty("contact_message")
  private ContactMessage contactMessage;
  // Required. The channel and contact channel identity of the message.
  @JsonProperty("channel_identity")
  private ChannelIdentity channelIdentity;
  // Required. The ID of the conversation.
  @JsonProperty("conversation_id")
  private String conversationId;
  // Required. The ID of the contact.
  @JsonProperty("contact_id")
  private String contactId;
  /* Optional. Metadata associated with the contact.
   * Up to 1024 characters long. */
  @Size(max = 1024)
  private String metadata;
  // Output only.
  @JsonProperty("accept_time")
  private String acceptTime;

  @Builder(builderMethodName = "fromAppMessage", builderClassName = "AppConversationMessage")
  public ConversationMessage(
      ConversationDirection direction,
      AppMessage appMessage,
      ChannelIdentity channelIdentity,
      String conversationId,
      String contactId,
      String metadata) {
    this.direction = direction;
    this.appMessage = appMessage;
    this.channelIdentity = channelIdentity;
    this.conversationId = conversationId;
    this.contactId = contactId;
    this.metadata = metadata;
  }

  @Builder(
      builderMethodName = "fromContactMessage",
      builderClassName = "ContactConversationMessage")
  public ConversationMessage(
      ConversationDirection direction,
      ContactMessage contactMessage,
      ChannelIdentity channelIdentity,
      String conversationId,
      String contactId,
      String metadata) {
    this.direction = direction;
    this.contactMessage = contactMessage;
    this.channelIdentity = channelIdentity;
    this.conversationId = conversationId;
    this.contactId = contactId;
    this.metadata = metadata;
  }
}
