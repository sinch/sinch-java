package com.sinch.sdk.restclient;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
class BodyMapper {

  static String bodyToString(byte[] responseBody) {
    return Optional.ofNullable(responseBody)
        .map(bodyBytes -> new String(bodyBytes, StandardCharsets.UTF_8))
        .orElse("");
  }
}
