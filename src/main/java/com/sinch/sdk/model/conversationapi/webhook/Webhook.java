package com.sinch.sdk.model.conversationapi.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Webhook
 *
 * Represents a destination for receiving callbacks from the Conversation API. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Webhook {

  // Output only. The ID of the webhook.
  private String id;
  // The app that this webhook belongs to.
  @NotBlank
  @JsonProperty("app_id")
  private String appId;
  /* The target url where events should be sent to.
   * Maximum URL length is 742. */
  @NotBlank
  @Size(max = 742)
  private String target;
  /* Optional secret be used to sign contents of webhooks sent by the Conversation API.
   * You can then use the secret to verify the signature. */
  private String secret;
  // Type of the target url.
  @NotBlank
  @JsonProperty("target_type")
  private Webhook.TargetType targetType;
  /* An array of triggers that should trigger the webhook and result in a
   * event being sent to the target url. */
  @JsonProperty("triggers")
  private List<Trigger> triggers;

  @Builder
  public Webhook(
      String appId, String target, String secret, TargetType targetType, List<Trigger> triggers) {
    this.appId = appId;
    this.secret = secret;
    this.target = target;
    this.targetType = targetType;
    this.triggers = triggers;
  }

  public enum TargetType {
    DISMISS,
    GRPC,
    HTTP
  }

  // An event triggering Conversation API callback
  public enum Trigger {
    // Using this value will cause errors.
    UNSPECIFIED_TRIGGER,
    // Subscribe to delivery receipts for a message sent.
    MESSAGE_DELIVERY,
    // Subscribe to delivery receipts for a event sent.
    EVENT_DELIVERY,
    // Subscribe to inbound messages from end users on the underlying channels.
    MESSAGE_INBOUND,
    // Subscribe to inbound events from end users on the underlying channels.
    EVENT_INBOUND,
    // Subscribe to an event that is triggered when a new conversation has been started.
    CONVERSATION_START,
    // Subscribe to an event that is triggered when a active conversation has been stopped.
    CONVERSATION_STOP,
    // Subscribe to an event that is triggered when a new contact has been created.
    CONTACT_CREATE,
    // Subscribe to an event that is triggered when a contact has been deleted.
    CONTACT_DELETE,
    // Subscribe to an event that is triggered when a two contacts are merged.
    CONTACT_MERGE,
    // Subscribe to callbacks that are not natively supported by the Conversation API.
    UNSUPPORTED,
    // Subscribe to opt_ins.
    OPT_IN,
    // Subscribe to opt_outs.
    OPT_OUT,
    // Subscribe to see get capability results.
    CAPABILITY
  }
}
