package com.sinch.sdk.model.conversationapi.message.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.common.enums.MessageQueue;
import com.sinch.sdk.model.conversationapi.message.AppMessage;
import java.time.Duration;
import java.util.List;
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
public class SendAppMessageRequest {
  // Required. The ID of the app sending the message.
  @JsonProperty("app_id")
  private String appId;

  /* Required. The recipient can be an existing contact or a list of channel identities.
   * When no contact for the specified project exists with the given channel identities
   * a new contact is created automatically. */
  private Recipient recipient;

  // Required. The message to send.
  private AppMessage message;

  /* Optional. Overwrites the default callback url for delivery reports for this message
   * The REST URL should be of the form: scheme://host[:port]/path
   * where scheme is http or https.
   * The gRPC URL should be of the form: dns://host[:port]
   * (See: https://github.com/grpc/grpc/blob/master/doc/naming.md). */
  @JsonProperty("callback_url")
  private String callbackUrl;

  /* Optional. Explicitly define the channels and order in which they are tried when sending the message.
   * Which channels the API will try and their priority is defined by:
   * 1. channel_priority_order if available.
   * 2. recipient.identified_by.channel_identities if available.
   * 3. When recipient is a contact_id:
   *   - if a conversation with the contact exists: the active channel of the conversation is tried first.
   *   - the existing channels for the contact are ordered by contact channel preferences if given.
   *   - lastly the existing channels for the contact are ordered by the app priority.
   **/
  @JsonProperty("channel_priority_order")
  private List<ConversationChannel> channelPriorityOrder;

  // Optional. Eventual metadata that should be associated to the message.
  @JsonProperty("message_metadata")
  private String messageMetadata;

  /* Optional. Define what queue the message should be placed in.
   * Note that most messages should be placed in the default NORMAL_PRIORITY.
   * QUEUE_HIGH_PRIORITY should only be used for messages that needs to start processing ASAP.
   * Note that rate limits still apply to both queues.
   **/
  private MessageQueue queue;

  /* Optional. Duration for trying to send the message.
   * Passed onto for channels which have support for it and
   * emulated by Conversation API for channels without ttl support
   * but message retract/unsend functionality.
   * Channel failover will not be performed for messages with
   * expired TTL. */
  // FIXME duration
  private Duration ttl;

  /* Optional. Channel-specific properties.
   * The key in the map must point to a valid channel property key as
   * defined by the enum ChannelPropertyKeys.
   * The maximum allowed property value length is 1024 characters. */
  @JsonProperty("channel_properties")
  private Map<String, String> channelProperties;
}
