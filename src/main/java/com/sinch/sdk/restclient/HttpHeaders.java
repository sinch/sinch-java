package com.sinch.sdk.restclient;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import lombok.Value;

@Value
public class HttpHeaders {
  private final Map<String, List<String>> headers;

  public HttpHeaders(Map<String, List<String>> headers) {
    this.headers = headers;
  }

  public Optional<String> firstValue(String headerName) {
    return Optional.ofNullable(headers.get(headerName))
        .map(List::listIterator)
        .filter(ListIterator::hasNext)
        .map(ListIterator::next);
  }
}
