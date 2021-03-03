package com.sinch.sdk.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

  public static boolean isEmpty(final String string) {
    return string == null || string.isEmpty();
  }
}
