package com.sinch.sdk.model.conversationapi.optin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Represents an explicit Opt-Out registration
 *
 * An Opt-Out contains the identity of the recipient which
 * retract its consent to receive messages from Conversation API apps
 * on a given channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptOut {

  // Required. The app for which the Opt-Out is being registered.
  @JsonProperty("app_id")
  @NotBlank
  private String appId;

  // Required. The contact or the list of channel identities opting out.
  @NotBlank private Recipient recipient;

  /* Required. The channels covered by this Opt-Out.
   * The default are all the channels for a contact if recipient is
   * contact_id or the channels in the channel_identities list.
   */
  @NotEmpty private List<ConversationChannel> channels;
}
