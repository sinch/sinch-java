package com.sinch.sdk.model.conversationapi.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Conversation
 *
 * A collection of messages exchanged between a contact and an app.
 * Conversations are normally created on the fly by Conversation API once
 * a message is sent and there is no active conversation already.
 * There can be only one active conversation at any given time between
 * a particular contact and an app.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
  // Output only. The ID of the conversation.
  private String id;

  // The ID of the participating app.
  @JsonProperty("app_id")
  private String appId;

  // The ID of the participating contact.
  @JsonProperty("contact_id")
  private String contactId;

  /* Output only. The timestamp of the latest message in the conversation. The timestamp will be
   * Thursday January 01, 1970 00:00:00 UTC if the conversation contains no messages. */
  // FIXME: protobuf timestamp
  @JsonProperty("last_received")
  private String lastReceived;

  /* Output only. The channel last used for communication in the conversation. The value will be
   * CHANNEL_UNSPECIFIED if the conversation does not contain messages. */
  @JsonProperty("active_channel")
  private ConversationChannel activeChannel;

  // Flag for whether this conversation is active.
  private Boolean active;

  /* An arbitrary data set by the Conversation API clients.
   * Up to 1024 characters long. */
  @Size(max = 1024)
  private String metadata;
}
