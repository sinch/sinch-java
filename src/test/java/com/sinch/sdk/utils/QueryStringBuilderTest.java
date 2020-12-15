package com.sinch.sdk.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class QueryStringBuilderTest {

  private static final String KEY_1 = "key_1";
  private static final String KEY_2 = "key_2";
  private static final String VALUE_1 = "value_1";
  private static final String VALUE_2 = "value_2";

  private static final String STRING = "%s";
  private static final String PATTERN_ONE_PARAM =
      QueryStringBuilder.PREFIX + STRING + QueryStringBuilder.KEY_VALUE_DELIMITER + STRING;
  private static final String PATTERN_TWO_PARAMS =
      PATTERN_ONE_PARAM
          + QueryStringBuilder.DELIMITER
          + STRING
          + QueryStringBuilder.KEY_VALUE_DELIMITER
          + STRING;

  @Test
  void testEmptyParams() {
    assertEquals("", QueryStringBuilder.newInstance().build());
  }

  @Test
  void testIgnoreNullValue() {
    assertEquals("", QueryStringBuilder.newInstance().add(KEY_1, null).build());
  }

  @Test
  void testOneParam() {
    final String queryString = QueryStringBuilder.newInstance().add(KEY_1, VALUE_1).build();
    assertEquals(String.format(PATTERN_ONE_PARAM, KEY_1, VALUE_1), queryString);
  }

  @Test
  void testTwoParam() {
    final String queryString =
        QueryStringBuilder.newInstance().add(KEY_1, VALUE_1).add(KEY_2, VALUE_2).build();
    assertEquals(String.format(PATTERN_TWO_PARAMS, KEY_1, VALUE_1, KEY_2, VALUE_2), queryString);
  }

  @Test
  void testOneParamNotString() {
    assertEquals(
        String.format(PATTERN_ONE_PARAM, KEY_1, "5"),
        QueryStringBuilder.newInstance().add(KEY_1, 5).build());
  }
}
