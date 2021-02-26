package com.sinch.sdk.restclient;

import static com.sinch.sdk.restclient.BodyMapper.bodyToString;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.exception.ConfigurationException;
import java.net.URI;
import lombok.Value;
import org.slf4j.Logger;

class ResponseValidator {

  byte[] validate(ResponseMetadata responseMetadata, Logger logger) {
    try {
      if (responseMetadata.isUnauthorized()) {
        throw new ConfigurationException(
            "Invalid credentials, verify the keyId and keySecret",
            bodyToString(responseMetadata.getBody()));
      }
      if (responseMetadata.isError()) {
        throw new ApiException(
            responseMetadata.getStatusCode(),
            "Call to " + responseMetadata.getRequestURI() + " received non-success response",
            responseMetadata.getHttpHeaders(),
            bodyToString(responseMetadata.getBody()));
      }
      return responseMetadata.getBody();
    } finally {
      if (responseMetadata.isError()) {
        logger.error(
            "Received response from {} with status code: {}",
            responseMetadata.getRequestURI(),
            responseMetadata.getStatusCode());
      }
      if (logger.isDebugEnabled()) {
        logger.debug(
            "Received response from {} with status code {} and body: {}",
            responseMetadata.getRequestURI(),
            responseMetadata.getStatusCode(),
            bodyToString(responseMetadata.getBody()));
      }
    }
  }

  @Value
  static class ResponseMetadata {
    int statusCode;
    URI requestURI;
    byte[] body;
    HttpHeaders httpHeaders;

    boolean isUnauthorized() {
      return statusCode == HTTP_UNAUTHORIZED;
    }

    boolean isError() {
      return statusCode < 200 || statusCode >= 399;
    }
  }
}
