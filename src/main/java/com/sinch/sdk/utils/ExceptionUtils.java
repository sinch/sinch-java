package com.sinch.sdk.utils;

import com.sinch.sdk.exception.ApiException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionUtils {

  public static final int BAD_REQUEST = 400;

  public <T> CompletableFuture<T> missingParam(final String paramName) {
    return CompletableFuture.failedFuture(
        new ApiException(
            BAD_REQUEST, String.format("Missing the required parameter '%s'", paramName)));
  }

  public <T> CompletableFuture<T> missingOneOf(final String... paramNames) {
    return CompletableFuture.failedFuture(
        new ApiException(
            BAD_REQUEST,
            String.format(
                "Missing required oneOf parameter: %s",
                Arrays.stream(paramNames).collect(Collectors.toList()))));
  }

  @SneakyThrows
  public ApiException getExpectedCause(final Throwable throwable) {
    final Throwable cause = throwable.getCause();
    if (cause instanceof ApiException) {
      final ApiException apiException = (ApiException) cause; // TODO: replace with proper logging
      System.out.println(apiException.getCode());
      System.out.println(apiException.getResponseBody());
      return apiException;
    }
    throw throwable;
  }

  @SneakyThrows
  public String getResponseBody(final HttpResponse<InputStream> response) {
    return response.body() == null ? null : new String(response.body().readAllBytes());
  }
}
