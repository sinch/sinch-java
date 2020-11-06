package com.sinch.sdk.client.auth;

import lombok.Getter;

public enum AuthMode {
  BASIC_AUTH("Basic %s"),
  BEARER_TOKEN("Bearer %s");

  @Getter private String template;

  AuthMode(String template) {
    this.template = template;
  }
}
