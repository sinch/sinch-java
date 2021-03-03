package com.sinch.sdk.utils;

import com.sinch.sdk.exception.ApiException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionUtils {

  public static final int BAD_REQUEST = 400;

  public <T> CompletableFuture<T> missingParam(final String paramName) {
    final CompletableFuture<T> completableFuture = new CompletableFuture<>();
    completableFuture.completeExceptionally(
        new ApiException(
            BAD_REQUEST, String.format("Missing the required parameter '%s'", paramName)));
    return completableFuture;
  }

  public <T> CompletableFuture<T> missingOneOf(final String... paramNames) {
    final CompletableFuture<T> completableFuture = new CompletableFuture<>();
    completableFuture.completeExceptionally(
        new ApiException(
            BAD_REQUEST,
            String.format(
                "Missing required oneOf parameter: %s",
                Arrays.stream(paramNames).collect(Collectors.toList()))));
    return completableFuture;
  }

  @SneakyThrows
  public ApiException getExpectedCause(final Throwable throwable) {
    final Throwable cause = throwable.getCause();
    if (cause instanceof ApiException) {
      return (ApiException) cause;
    }
    throw throwable;
  }
}
