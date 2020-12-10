package com.sinch.sdk.model.common;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Region {
  EU,
  US;

  private static final Map<String, Region> LOOKUP =
      Arrays.stream(values()).collect(Collectors.toMap(Region::nameLowercase, Function.identity()));

  public static Region safeValueOf(final String value) {
    return Optional.ofNullable(value).map(String::toLowerCase).map(LOOKUP::get).orElse(null);
  }

  public String nameLowercase() {
    return name().toLowerCase();
  }
}
