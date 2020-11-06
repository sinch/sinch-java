package com.sinch.sdk.model.conversationapi.event.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.common.enums.MessageQueue;
import com.sinch.sdk.model.conversationapi.event.ConversationEvent.AppEvent;
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
public class SendEventRequest {
  // Required. The ID of the app sending the event.
  @JsonProperty("app_id")
  private String appId;

  /* Required. The recipient can be an existing contact or a channel identity
   * When no contact for that project exists with the given channel identity
   * a new contact is created automatically. */
  private Recipient recipient;

  // Required. The event to send.
  private AppEvent event;

  /* Optional. Overwrites the default callback url for delivery reports for this event
   *The REST URL should be of the form: scheme://host[:port]/path
   * where scheme is http or https.
   * The gRPC URL should be of the form: dns://host[:port]
   * (See: https://github.com/grpc/grpc/blob/master/doc/naming.md). */
  @JsonProperty("callback_url")
  private String callbackUrl;

  /* Optional. Channel priority order that dictates on which channels the
   * Conversation API should try to send the message on. The order provided
   * here overrides any default. */
  @JsonProperty("channel_priority_order")
  private List<ConversationChannel> channelPriorityOrder;

  // Optional. Eventual metadata that should be associated to the event.
  @JsonProperty("event_metadata")
  private String eventMetadata;

  /* Optional. Define what queue the message should be placed in.
   * Note that most messages should be placed in the default QUEUE_NORMAL_PRIORITY.
   * QUEUE_HIGH_PRIORITY should only be used for messages that needs to be processed ASAP.
   * Note that rate limits still apply to both queues.
   **/
  private MessageQueue queue;
}
