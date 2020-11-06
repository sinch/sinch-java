package com.sinch.sdk.model.conversationapi.common.enums;

/* Possible keys for the channel_properties map
 * The entries have the following format: <conversation channel>_<property name>. */
public enum ChannelPropertyKeys {
  PROPERTY_KEY_UNDEFINED,

  /* Messenger messaging type.
   * For more information visit:
   * https://developers.facebook.com/docs/messenger-platform/send-messages/#messaging_types
   * Defaults to MESSAGE_TAG if MESSENGER_MESSAGE_TAG is set. */
  MESSENGER_MESSAGING_TYPE,

  /* Messenger message tag. Enables sending specific updates
   * to users outside the standard messaging window.
   * For more information including a list of possible values visit:
   * https://developers.facebook.com/docs/messenger-platform/send-messages/message-tags
   * There is no default value for this property. */
  MESSENGER_MESSAGE_TAG,

  /* Messenger push notification type.
   * Possible values are REGULAR (sound/vibration),
   * SILENT_PUSH (on-screen notification only) and
   * NO_PUSH (no notification).
   * The default is REGULAR. */
  MESSENGER_NOTIFICATION_TYPE,

  /* Viber Bot sender’s name to display.
   * Max 28 characters. */
  VIBER_SENDER_NAME,

  /* Viber Bot sender’s avatar URL.
   * Avatar size should be no more than 100 kb.
   * Recommended 720x720. */
  VIBER_SENDER_AVATAR,

  /* Whether this is flash SMS message.
   * Flash SMS messages are shown on screen without user interaction
   * while not saving the message to the inbox.
   * Possible values are true and false.
   * The default is false. */
  SMS_FLASH_MESSAGE
}
