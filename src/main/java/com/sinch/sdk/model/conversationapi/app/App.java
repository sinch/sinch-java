package com.sinch.sdk.model.conversationapi.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.common.ConversationChannelCredential;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Conversation API app
 *
 * The app corresponds to the API user and is a collection of channel credentials
 * allowing access to the underlying messaging channels.
 * The app is tied to a set of webhooks which define the destination for various events coming from the Conversation API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class App {

  // Output only. The ID of the app.
  private String id;
  /* Channel credentials.
   * The order of the credentials defines the
   * app channel priority. */
  @JsonProperty("channel_credentials")
  private List<ConversationChannelCredential> channelCredentials;
  /* Optional. A flag specifying whether to return conversation metadata as
   * part of each callback. If omitted NONE will be used. */
  @JsonProperty("conversation_metadata_report_view")
  private ConversationMetadataReportView conversationMetadataReportView;
  // Human readable identifier of the app. E.g. Sinch Conversation API Demo App 001.
  @JsonProperty("display_name")
  private String displayName;
  // Output only. Rate limits associated with the app. Contact your account manager to change these.
  @JsonProperty("rate_limits")
  private RateLimits rateLimits;
  // Optional. Defines the retention policy for messages and conversations.
  @JsonProperty("retention_policy")
  private RetentionPolicy retentionPolicy;

  @Builder
  public App(
      List<ConversationChannelCredential> channelCredentials,
      ConversationMetadataReportView conversationMetadataReportView,
      String displayName,
      RetentionPolicy retentionPolicy) {
    this.channelCredentials = channelCredentials;
    this.conversationMetadataReportView = conversationMetadataReportView;
    this.displayName = displayName;
    this.retentionPolicy = retentionPolicy;
  }

  public enum ConversationMetadataReportView {
    // Omit metadata
    NONE,
    // Include all metadata assigned to the conversation
    FULL
  }

  public enum RetentionPolicyType {
    /* The default retention policy where messages older than
     * ttl_days are automatically deleted from Conversation API database. */
    MESSAGE_EXPIRE_POLICY,
    /* The conversation expire policy only considers the last message in a conversation.
     * If the last message is older that ttl_days the entire conversation is deleted.
     * The difference with MESSAGE_EXPIRE_POLICY is that messages with accept_time
     * older than ttl_days are persisted as long as there is a newer message in the
     * same conversation. */
    CONVERSATION_EXPIRE_POLICY,
    /* Persist policy does not delete old messages or conversations.
     * Please note that message storage might be subject to additional charges
     * in the future. */
    PERSIST_RETENTION_POLICY
  }

  @Data
  @NoArgsConstructor
  public static class RateLimits {
    /* Output only. The number of messages/events we process per second, from the
     * app to the underlying channels. Note that underlying channels may have other
     * rate limits.  The default rate limit is 25.*/
    private int outbound;
    /* Output only. The number of inbound messages/events we process per second,
     * from underlying channels to the app.  The default rate limit is 25.*/
    private int inbound;
    /* Output only. The rate limit of callbacks sent to the webhooks registered
     * for the app. Note that if you have multiple webhooks with shared triggers,
     * multiple callbacks will be sent out for each triggering event. The default rate limit is 25.*/
    private int webhooks;
  }

  // Retention policy for messages and conversations
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RetentionPolicy {
    /* Optional. Whether or not old messages or conversations are automatically deleted. */
    @JsonProperty("retention_type")
    private RetentionPolicyType retentionType;

    /* Optional. The days before a message or conversation is eligible for deletion.
     * Default value is 180. The ttl_days value has no effect when retention_type
     * is PERSIST_RETENTION_POLICY. The valid values for this field are [1 - 3650].
     * Note that retention cleanup job runs once every twenty-four hours
     * which can lead to delay i.e., messages and conversations are not deleted on
     * the minute they become eligible for deletion. */
    @JsonProperty("ttl_days")
    private int ttlDays;
  }
}
