package com.sinch.sdk.model.conversationapi.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reason {
  /* The code is a high-level classification of the error.
   * UNKNOWN is used if no other code can be used to describe the encountered error. */
  private ReasonCode code;

  // A textual description of the reason.
  private String description;

  public enum ReasonCode {
    // UNKNOWN is used if no other code can be used to describe the encountered error.
    UNKNOWN,

    /* An internal error occurred. Please save the entire callback if you want to
     * report an error. */
    INTERNAL_ERROR,

    // The message or event was not sent due to rate limiting.
    RATE_LIMITED,

    // The channel recipient identity was malformed.
    RECIPIENT_INVALID_CHANNEL_IDENTITY,

    /* It was not possible to reach the contact, or channel recipient identity,
     * on the channel. */
    RECIPIENT_NOT_REACHABLE,

    // The contact, or channel recipient identity, has not opt-ed in on the channel.
    RECIPIENT_NOT_OPTED_IN,

    /* The allowed sending window has expired. See the channel documentation
     * for more information about how the sending window works for the different
     * channels. */
    OUTSIDE_ALLOWED_SENDING_WINDOW,

    /* The channel failed to accept the message. The Conversation API performs
     * multiple retries in case of transient errors */
    CHANNEL_FAILURE,

    /* The configuration of the channel for the used App is wrong. The bad
     * configuration caused the channel to reject the message. Please see
     * the channel support documentation page for how to set it up correctly. */
    CHANNEL_BAD_CONFIGURATION,

    /* The configuration of the channel is missing from the used App. Please see
     * the channel support documentation page for how to set it up correctly. */
    CHANNEL_CONFIGURATION_MISSING,

    /* Some of the referenced media files is of a unsupported media type. Please
     * read the channel support documentation page to find out the limitations
     * on media that the different channels impose. */
    MEDIA_TYPE_UNSUPPORTED,

    /* Some of the referenced media files are too large. Please read the channel
     * support documentation to find out the limitations on file size that the
     * different channels impose. */
    MEDIA_TOO_LARGE,

    /* The provided media link was not accessible from the Conversation API or
     * from the underlying channels. Please make sure that the media file is
     * accessible. */
    MEDIA_NOT_REACHABLE,

    /* No channels to try to send the message to. This error will occur if one
     * attempts to send a message to a channel with no channel identities or if
     * all applicable channels have been attempted. */
    NO_CHANNELS_LEFT,

    // The referenced template was not found.
    TEMPLATE_NOT_FOUND,

    /* Sufficient template parameters was not given. All parameters defined
     * in the template must be provided when sending a template message */
    TEMPLATE_INSUFFICIENT_PARAMETERS,

    /* The selected language, or version, of the referenced template did
     * not exist. Please check the available versions and languages of the template */
    TEMPLATE_NON_EXISTING_LANGUAGE_OR_VERSION,

    /* The message delivery, or event delivery, failed due to a channel-imposed timeout.
     * See the channel support documentation page for further details
     * about how the different channels behave. */
    DELIVERY_TIMED_OUT,

    /* The message or event was rejected by the channel due to a policy.
     * Some channels have specific policies that must be met to send a message.
     * See the channel support documentation page for more information about
     * when this error will be triggered. */
    DELIVERY_REJECTED_DUE_TO_POLICY,

    // The provided Contact ID did not exist.
    CONTACT_NOT_FOUND,

    /* Conversation API validates send requests in two different stages.
     * The first stage is right before the message is enqueued.
     * If this first validation fails the API responds with 400 Bad Request
     * and the request is discarded immediately.
     * The second validation kicks in during message processing and
     * it normally contains channel specific validation rules.
     * Failures during second request validation are
     * delivered as callbacks to MESSAGE_DELIVERY (EVENT_DELIVERY) webhooks
     * with ReasonCode BAD_REQUEST. */
    BAD_REQUEST,

    /* The used App is missing. This error may occur when the app is removed
     * during message processing. */
    UNKNOWN_APP,

    /* The contact has no channel identities setup, or the contact has no
     * channels setup for the resolved channel priorities. */
    NO_CHANNEL_IDENTITY_FOR_CONTACT
  }
}
