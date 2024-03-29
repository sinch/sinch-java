package com.sinch.sdk.configuration;

import com.sinch.sdk.configuration.impl.ConfigurationEU;
import com.sinch.sdk.configuration.impl.ConfigurationUS;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.model.Region;
import java.time.Duration;

public interface Configuration {

  Authentication authentication();

  ConversationApi conversationApi();

  Duration httpTimeout();

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

    long getFallbackRetryDelay();

    boolean useBasicAuth();
  }

  interface ConversationApi {
    String getUrl();
  }
}
