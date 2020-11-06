package com.sinch.sdk.model.conversationapi.message.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Message referring to predefined template
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TemplateMessage {

  /* Optional. Channel specific template reference with parameters per channel.
   * The channel template if exists overrides the omnichannel template.
   * At least one of channel_template or omni_template needs to be present.
   * The key in the map must point to a valid conversation channel as
   * defined by the enum ConversationChannel. */
  @JsonProperty("channel_template")
  private Map<String, TemplateReference> channelTemplate;
  /* Optional. Omnichannel template stored in Conversation API Template Store
   * as AppMessage. At least one of channel_template or omni_template needs to be present. */
  @JsonProperty("omni_template")
  private TemplateReference omniTemplate;

  @Builder(builderMethodName = "fromChannelTemplate", builderClassName = "ChannelTemplateMessage")
  public TemplateMessage(Map<String, TemplateReference> channelTemplate) {
    this.channelTemplate = channelTemplate;
  }

  @Builder(builderMethodName = "fromOmniTemplate", builderClassName = "OmniTemplateMessage")
  public TemplateMessage(TemplateReference omniTemplate) {
    this.omniTemplate = omniTemplate;
  }

  /* Template reference with concrete parameter values
   *
   * The referenced template can be an omnichannel template
   * stored in Conversation API Template Store as AppMessage
   * or it can reference external channel-specific template
   * such as WhatsApp Business Template. */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TemplateReference {
    // Required. The ID of the template.
    @JsonProperty("template_id")
    private String templateId;

    /* Required. Used to specify what version of a template to use.
     * This will be used in conjunction with language_code. */
    private String version;

    /* Optional. The BCP-47 language code, such as "en-US" or "sr-Latn".
     * For more information, see http://www.unicode.org/reports/tr35/#Unicode_locale_identifier.
     * English is the default language_code. */
    @JsonProperty("language_code")
    private String languageCode;

    /* Optional. Required if the template
     * has parameters. Concrete values must
     * be present for all defined parameters
     * in the template. Parameters can be different for
     * different versions and/or languages of the template. */
    private Map<String, String> parameters;
  }
}
