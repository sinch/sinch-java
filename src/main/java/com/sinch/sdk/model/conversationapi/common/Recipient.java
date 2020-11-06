package com.sinch.sdk.model.conversationapi.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Recipient
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Recipient {

  // The ID of the receiving contact.
  @JsonProperty("contact_id")
  private String contactId;
  // The recipient identity on given channels.
  @JsonProperty("identified_by")
  private IdentifiedBy identifiedBy;

  @Builder(builderMethodName = "fromContactId", builderClassName = "ContactIdRecipient")
  public Recipient(String contactId) {
    this.contactId = contactId;
  }

  @Builder(builderMethodName = "fromChannelIdentity", builderClassName = "ChannelIdentityRecipient")
  public Recipient(IdentifiedBy identifiedBy) {
    this.identifiedBy = identifiedBy;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class IdentifiedBy {
    /* A list of specific channel identities.
     * The API will use these identities when sending to specific channels. */
    @JsonProperty("channel_identities")
    private List<ChannelRecipientIdentity> channelIdentities;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChannelRecipientIdentity {
    // Required. The channel.
    @NotBlank private ConversationChannel channel;

    // Required. The channel recipient identity. E.g a phone number.
    @NotBlank private String identity;
  }
}
