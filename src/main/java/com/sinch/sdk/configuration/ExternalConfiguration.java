package com.sinch.sdk.configuration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExternalConfiguration {

  public static String getProjectId() {
    return System.getProperty(Keys.PROJECT_ID);
  }

  public static String getKeyId() {
    return System.getProperty(Keys.KEY_ID);
  }

  public static String getKeySecret() {
    return System.getProperty(Keys.KEY_SECRET);
  }

  private static class Keys {
    private static final String KEY_PREFIX = "sinch.";
    private static final String PROJECT_ID = KEY_PREFIX + "project_id";
    private static final String KEY_ID = KEY_PREFIX + "key_id";
    private static final String KEY_SECRET = KEY_PREFIX + "key_secret";
  }

  public static class Authentication {

    public static String getUrl() {
      return System.getProperty(Keys.URL);
    }

    public static Long getHttpTimeout() {
      try {
        return Long.parseLong(System.getProperty(Keys.HTTP_TIMEOUT));
      } catch (final NumberFormatException ignored) {
      }
      return null;
    }

    private static class Keys {
      private static final String KEY_PREFIX =
          ExternalConfiguration.Keys.KEY_PREFIX + "authentication.";
      private static final String URL = KEY_PREFIX + "url";
      private static final String HTTP_TIMEOUT = KEY_PREFIX + "http_timeout";
    }
  }

  public static class ConversationApi {

    public static String getUrl() {
      return System.getProperty(Keys.URL);
    }

    private static class Keys {
      private static final String KEY_PREFIX =
          ExternalConfiguration.Keys.KEY_PREFIX + "conversationApi.";
      private static final String URL = KEY_PREFIX + "url";
    }
  }
}
