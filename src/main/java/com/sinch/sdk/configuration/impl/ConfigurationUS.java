package com.sinch.sdk.configuration.impl;

import lombok.Getter;

@Getter
public class ConfigurationUS extends AbstractConfiguration {

  @Override
  public Authentication authentication() {
    return new AuthenticationUS();
  }

  @Override
  public ConversationApi conversationApi() {
    return new ConversationApiUS();
  }

  @Getter
  public static class AuthenticationUS extends AbstractAuthentication {
    private final String urlInternal = "https://us.auth.sinch.com/oauth2/token";
  }

  @Getter
  public static class ConversationApiUS extends AbstractConfigurationApi {
    private final String urlInternal = "https://us.conversation.api.sinch.com";
  }
}
