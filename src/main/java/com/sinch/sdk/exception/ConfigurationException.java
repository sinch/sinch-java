package com.sinch.sdk.exception;

public class ConfigurationException extends RuntimeException {

  private final String details;

  public ConfigurationException(final String message) {
    super(message);
    details = null;
  }

  public ConfigurationException(final String message, final String details) {
    super(message);
    this.details = details;
  }
}
