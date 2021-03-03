package com.sinch.sdk.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryStringBuilder {

  public static final String PREFIX = "?";
  public static final String DELIMITER = "&";
  public static final String SUFFIX = "";
  public static final String KEY_VALUE_DELIMITER = "=";

  private final Map<String, Object> queryParams;

  private QueryStringBuilder() {
    queryParams = new LinkedHashMap<>();
  }

  public QueryStringBuilder add(final String key, final Object value) {
    Optional.ofNullable(value).ifPresent(v -> queryParams.put(key, v));
    return this;
  }

  public String build() {
    return queryParams.isEmpty()
        ? ""
        : queryParams.entrySet().stream()
            .map(
                entry ->
                    entry.getKey().concat(KEY_VALUE_DELIMITER).concat(entry.getValue().toString()))
            .collect(Collectors.joining(DELIMITER, PREFIX, SUFFIX));
  }

  public static QueryStringBuilder newInstance() {
    return new QueryStringBuilder();
  }
}
