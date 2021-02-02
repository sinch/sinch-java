package com.sinch.sdk.configuration.impl;

import com.sinch.sdk.configuration.Configuration;
import com.sinch.sdk.configuration.ExternalConfiguration;
import java.util.Optional;

public abstract class AbstractConfiguration implements Configuration {

  public abstract static class AbstractAuthentication implements Authentication {
    @Override
    public String getUrl() {
      return Optional.ofNullable(ExternalConfiguration.Authentication.getUrl())
          .orElse(getUrlInternal());
    }

    protected abstract String getUrlInternal();

    @Override
    public long getHttpTimeout() {
      return Optional.ofNullable(ExternalConfiguration.Authentication.getHttpTimeout()).orElse(10L);
    }

    @Override
    public boolean useBasicAuth() {
      return ExternalConfiguration.Authentication.getBasicAuth();
    }

    @Override
    public long getFallbackRetryDelay() {
      return 10L;
    }
  }

  public abstract static class AbstractConfigurationApi implements ConversationApi {
    @Override
    public String getUrl() {
      return Optional.ofNullable(ExternalConfiguration.ConversationApi.getUrl())
          .orElse(getUrlInternal());
    }

    protected abstract String getUrlInternal();
  }
}
