package com.sinch.sdk.model.conversationapi.common.enums;

/* Message/Event Status
 *
 * Note that not all statuses
 * are sent by the different channels. */
public enum Status {
  STATUS_UNSPECIFIED,
  QUEUED,
  QUEUED_ON_CHANNEL,
  DELIVERED,
  READ,
  FAILED,
  SWITCHING_CHANNEL
}
