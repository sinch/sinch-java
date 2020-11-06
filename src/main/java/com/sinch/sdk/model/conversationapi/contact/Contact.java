package com.sinch.sdk.model.conversationapi.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Contact
 *
 * A participant in a conversation typically representing a person.
 * It is associated with a collection of channel identities.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Contact {

  // Output only. The ID of the contact.
  private String id;
  // Optional. The display name. A default 'Unknown' will be assigned if left empty
  @JsonProperty("display_name")
  private String displayName;
  // Optional. Email of the contact.
  private String email;
  // Optional. Contact identifier in an external system.
  @JsonProperty("external_id")
  private String externalId;
  /* Optional. Metadata associated with the contact.
   * Up to 1024 characters long. */
  @Size(max = 1024)
  private String metadata;
  // 2 letter ISO 639 language code for the contact. Default value is UNSPECIFIED language.
  private ContactLanguage language;
  // List of channels defining the channel priority.
  @JsonProperty("channel_priority")
  private List<ConversationChannel> channelPriority;
  // List of channel identities.
  @JsonProperty("channel_identities")
  private List<ChannelIdentity> channelIdentities;

  @Builder(toBuilder = true)
  public Contact(
      String displayName,
      String email,
      String externalId,
      String metadata,
      ContactLanguage language,
      List<ConversationChannel> channelPriority,
      List<ChannelIdentity> channelIdentities) {
    this.displayName = displayName;
    this.email = email;
    this.externalId = externalId;
    this.metadata = metadata;
    this.language = language;
    this.channelPriority = channelPriority;
    this.channelIdentities = channelIdentities;
  }

  public enum ContactLanguage {
    UNSPECIFIED,
    AF,
    SQ,
    AR,
    AZ,
    BN,
    BG,
    CA,
    ZH,
    ZH_CN,
    ZH_HK,
    ZH_TW,
    HR,
    CS,
    DA,
    NL,
    EN,
    EN_GB,
    EN_US,
    ET,
    FIL,
    FI,
    FR,
    DE,
    EL,
    GU,
    HA,
    HE,
    HI,
    HU,
    ID,
    GA,
    IT,
    JA,
    KN,
    KK,
    KO,
    LO,
    LV,
    LT,
    MK,
    MS,
    ML,
    MR,
    NB,
    FA,
    PL,
    PT,
    PT_BR,
    PT_PT,
    PA,
    RO,
    RU,
    SR,
    SK,
    SL,
    ES,
    ES_AR,
    ES_ES,
    ES_MX,
    SW,
    SV,
    TA,
    TE,
    TH,
    TR,
    UK,
    UR,
    UZ,
    VI,
    ZU
  }

  /* Channel Identity
   *
   * A unique identity of message recipient on a particular channel.
   * For example, the channel identity on SMS, WHATSAPP or VIBERBM is a MSISDN phone number.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChannelIdentity {

    // Required. The channel.
    @NotBlank private ConversationChannel channel;

    // Required. The channel identity e.g., a phone number for SMS, WhatsApp and Viber Business.
    @NotBlank private String identity;

    /* Optional. The Conversation API's app ID if this is app-scoped channel identity.
     * Currently, FB Messenger and Viber are using app-scoped channel identities
     * which means contacts will have different channel identities for different
     * apps. FB Messenger uses PSIDs (Page-Scoped IDs) as channel identities.
     * The app_id is pointing to the app linked to the FB page for which this PSID is issued.
     */
    @JsonProperty("app_id")
    private String appId;
  }
}
