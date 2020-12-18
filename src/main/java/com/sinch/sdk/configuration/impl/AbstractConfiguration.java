package com.sinch.sdk.configuration.impl;

import com.sinch.sdk.configuration.Configuration;
import com.sinch.sdk.configuration.ExternalConfiguration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    public long getFallbackRetryDelay() {
      return TimeUnit.MINUTES.toSeconds(5);
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
