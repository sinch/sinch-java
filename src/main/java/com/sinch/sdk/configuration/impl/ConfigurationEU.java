package com.sinch.sdk.configuration.impl;

import lombok.Getter;

@Getter
public class ConfigurationEU extends AbstractConfiguration {

  @Override
  public Authentication authentication() {
    return new AuthenticationEU();
  }

  @Override
  public ConversationApi conversationApi() {
    return new ConversationApiEU();
  }

  @Getter
  public static class AuthenticationEU extends AbstractAuthentication {
    private final String urlInternal = "https://eu.auth.sinch.com/oauth2/token";
  }

  @Getter
  public static class ConversationApiEU extends AbstractConfigurationApi {
    private final String urlInternal = "https://eu.conversation.api.sinch.com";
  }
}
