package com.sinch.sdk.configuration;

import com.sinch.sdk.configuration.impl.ConfigurationEU;
import com.sinch.sdk.configuration.impl.ConfigurationUS;
import com.sinch.sdk.model.common.Region;
import javax.naming.ConfigurationException;
import lombok.SneakyThrows;

public interface Configuration {

  Authentication authentication();

  ConversationApi conversationApi();

  @SneakyThrows
  static Configuration forRegion(final Region region) {
    switch (region) {
      case EU:
        return new ConfigurationEU();
      case US:
        return new ConfigurationUS();
      default:
        throw new ConfigurationException("Unsupported Region");
    }
  }

  interface Authentication {
    String getUrl();

    long getHttpTimeout();

    long getFallbackRetryDelay();
  }

  interface ConversationApi {
    String getUrl();
  }
}
