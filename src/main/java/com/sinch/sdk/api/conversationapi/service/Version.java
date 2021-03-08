package com.sinch.sdk.api.conversationapi.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class Version {

  private Version() {}

  static String getValue() {
    final Properties properties = new Properties();
    try (InputStream input =
        Version.class.getClassLoader().getResourceAsStream("com/sinch/version.properties")) {
      properties.load(input);
      return properties.getProperty("version");
    } catch (IOException e) {
      log.error("Cannot read SDK version", e);
      return "undefined";
    }
  }
}
