package com.sinch.sdk.model.conversationapi.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Channel Credential
 *
 * Enables access to the underlying messaging channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class ConversationChannelCredential {

  // Required. The identifier of the messaging channel.
  private ConversationChannel channel;
  // The channel credential enabling access to the underlying messaging channel. Either static
  // bearer or static token.
  @JsonProperty("static_bearer")
  private StaticBearer staticBearer;
  // The channel credential enabling access to the underlying messaging channel. Either static
  // bearer or static token.
  @JsonProperty("static_token")
  private StaticToken staticToken;

  @Builder(builderMethodName = "fromStaticToken", builderClassName = "StaticTokenCredential")
  public ConversationChannelCredential(ConversationChannel channel, StaticToken staticToken) {
    this.channel = channel;
    this.staticToken = staticToken;
  }

  @Builder(builderMethodName = "fromStaticBearer", builderClassName = "StaticBearerCredential")
  public ConversationChannelCredential(ConversationChannel channel, StaticBearer staticBearer) {
    this.channel = channel;
    this.staticBearer = staticBearer;
  }

  /* Bearer Channel Credential
   *
   * It consists of claimed identity and a static token.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class StaticBearer {
    // Required. Claimed identity.
    @NotBlank
    @JsonProperty("claimed_identity")
    private String claimedIdentity;

    // Required. Static bearer token.
    private String token;
  }

  // Static Token Credential
  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class StaticToken {
    // Required. The static token.
    @NotBlank private String token;
  }
}
