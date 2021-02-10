package com.sinch.sdk.exception;

import java.net.http.HttpHeaders;

public class ApiException extends RuntimeException {
  private final int code;
  private HttpHeaders responseHeaders = null;
  private String responseBody = null;

  public ApiException(final int code, final String message) {
    super(message);
    this.code = code;
  }

  public ApiException(
      final int code,
      final String message,
      final HttpHeaders responseHeaders,
      final String responseBody) {
    this(code, message);
    this.responseHeaders = responseHeaders;
    this.responseBody = responseBody;
  }

  /**
   * Get the HTTP status code.
   *
   * @return HTTP status code
   */
  public int getCode() {
    return code;
  }

  /**
   * Get the HTTP response headers.
   *
   * @return Headers as an HttpHeaders object
   */
  public HttpHeaders getResponseHeaders() {
    return responseHeaders;
  }

  /**
   * Get the HTTP response body.
   *
   * @return Response body in the form of string
   */
  public String getResponseBody() {
    return responseBody;
  }
}
