package com.sinch.sdk.model.conversationapi.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationDirection;
import com.sinch.sdk.model.conversationapi.contact.Contact.ChannelIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Conversation Event
 *
 * An event on a particular channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationEvent {

  /* The direction defines what is the source and
   * what is the destination of the event. */
  private ConversationDirection direction;

  @JsonProperty("app_event")
  private AppEvent appEvent;

  @JsonProperty("contact_event")
  private ContactEvent contactEvent;

  // Required. The ID of the contact.
  @JsonProperty("contact_id")
  private String contactId;

  // Required. The channel and contact channel identity of the event.
  @JsonProperty("channel_identity")
  private ChannelIdentity channelIdentity;

  // Output only.
  @JsonProperty("accept_time")
  private String acceptTime;

  // Message originating from an app
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AppEvent {
    @JsonProperty("composing_event")
    private ComposingEvent composingEvent;
  }

  // Message originating from a contact
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ContactEvent {
    @JsonProperty("composing_event")
    private ComposingEvent composingEvent;
  }

  @Data
  @Builder
  @NoArgsConstructor
  public static class ComposingEvent {
    // Required.
  }
}
