package com.sinch.sdk.api.authentication;

import com.sinch.sdk.configuration.Configuration;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.model.common.auth.service.AuthResponse;
import com.sinch.sdk.restclient.SinchRestClient;
import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;

public class AuthenticationService {
  public static final String HEADER_KEY_AUTH = "Authorization";
  private static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
  private static final String APPLICATION_FORM_URLENCODED_VALUE =
      "application/x-www-form-urlencoded";

  private static final String TEMPLATE_BEARER_AUTH = "Bearer %s";
  private static final String TEMPLATE_BASIC_AUTH = "Basic %s";
  private static final String TEMPLATE_ID_SECRET = "%s:%s";

  private final SinchRestClient sinchRestClient;
  private final URI authRequestURI;
  private final Map<String, String> authRequestHeaders;

  private long fallbackRetryDelay;
  private Timer refreshTimer;
  private CompletableFuture<Map<String, String>> authHeaderFuture;

  public AuthenticationService(
      final SinchRestClient sinchRestClient,
      final Configuration.Authentication config,
      @NonNull final String keyId,
      @NonNull final String keySecret) {
    this.sinchRestClient = sinchRestClient;
    String basicHeaderValue =
        String.format(
            TEMPLATE_BASIC_AUTH,
            Base64.getEncoder()
                .encodeToString(String.format(TEMPLATE_ID_SECRET, keyId, keySecret).getBytes()));
    this.authRequestURI = URI.create(config.getUrl());
    this.authRequestHeaders =
        Map.of(
            HEADER_KEY_AUTH,
            basicHeaderValue,
            HEADER_KEY_CONTENT_TYPE,
            APPLICATION_FORM_URLENCODED_VALUE);
    if (config.useBasicAuth()) {
      authHeaderFuture =
          CompletableFuture.completedFuture(Map.of(HEADER_KEY_AUTH, basicHeaderValue));
    } else {
      this.fallbackRetryDelay = config.getFallbackRetryDelay();
      this.refreshTimer = new Timer("BearerTokenRefreshTimer");
      reload();
    }
  }

  /**
   * Gets the header value
   *
   * <p>NOTE: fails with {@link ConfigurationException} if the response is 401, and {@link
   * com.sinch.sdk.exception.ApiException} for other http errors
   *
   * @return async task for the header value to use with @{HEADER_KEY_AUTH}
   */
  public CompletableFuture<Map<String, String>> getHeaderValue() {
    return authHeaderFuture;
  }

  private void reload() {
    authHeaderFuture =
        getAuthResponse()
            .thenApply(
                authResponse -> {
                  scheduleReload(Math.max(authResponse.getExpiresIn() - 60, fallbackRetryDelay));
                  return Map.of(
                      HEADER_KEY_AUTH,
                      String.format(TEMPLATE_BEARER_AUTH, authResponse.getAccessToken()));
                });
  }

  private CompletableFuture<AuthResponse> getAuthResponse() {
    return sinchRestClient.post(
        authRequestURI, AuthResponse.class, "grant_type=client_credentials", authRequestHeaders);
  }

  private void scheduleReload(final long delay) {
    refreshTimer.schedule(
        toTask(AuthenticationService.this::reload), TimeUnit.SECONDS.toMillis(delay));
  }

  private static TimerTask toTask(final Runnable runnable) {
    return new TimerTask() {
      @Override
      public void run() {
        runnable.run();
      }
    };
  }
}
