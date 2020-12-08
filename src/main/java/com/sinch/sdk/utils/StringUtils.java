package com.sinch.sdk.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

  public static boolean isEmpty(final String string) {
    return string == null || string.isEmpty();
  }
}
