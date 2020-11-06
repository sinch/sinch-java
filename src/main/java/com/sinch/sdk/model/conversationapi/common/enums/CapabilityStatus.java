package com.sinch.sdk.model.conversationapi.common.enums;

public enum CapabilityStatus {
  // The channel capability for the identity is unknown.
  CAPABILITY_UNKNOWN,
  // The specified identity supports all the features of the channel.
  CAPABILITY_FULL,
  // The specified identity supports a subset of the channel features.
  CAPABILITY_PARTIAL,
  // The specified identity has no capability on the channel.
  NO_CAPABILITY
}
