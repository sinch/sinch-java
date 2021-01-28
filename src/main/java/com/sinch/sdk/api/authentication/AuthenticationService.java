package com.sinch.sdk.api.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.configuration.Configuration;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.model.common.auth.service.AuthResponse;
import com.sinch.sdk.utils.ExceptionUtils;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.SneakyThrows;

public class AuthenticationService {
  public static final String HEADER_KEY_AUTH = "Authorization";
  private static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
  private static final String APPLICATION_FORM_URLENCODED_VALUE =
      "application/x-www-form-urlencoded";

  private static final String TEMPLATE_BEARER_AUTH = "Bearer %s";
  private static final String TEMPLATE_BASIC_AUTH = "Basic %s";
  private static final String TEMPLATE_ID_SECRET = "%s:%s";

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  private final long fallbackRetryDelay;
  private final Timer refreshTimer;
  private final HttpRequest bearerTokenRequest;
  private CompletableFuture<String> authHeaderFuture;

  public AuthenticationService(
      final HttpClient httpClient,
      final ObjectMapper objectMapper,
      final Configuration.Authentication config,
      @NonNull final String keyId,
      @NonNull final String keySecret) {
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
    this.fallbackRetryDelay = config.getFallbackRetryDelay();
    this.refreshTimer = new Timer("BearerTokenRefreshTimer");
    this.bearerTokenRequest =
        HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
            .uri(URI.create(config.getUrl()))
            .header(
                HEADER_KEY_AUTH,
                String.format(
                    TEMPLATE_BASIC_AUTH,
                    Base64.getEncoder()
                        .encodeToString(
                            String.format(TEMPLATE_ID_SECRET, keyId, keySecret).getBytes())))
            .header(HEADER_KEY_CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .timeout(Duration.ofSeconds(config.getHttpTimeout()))
            .build();
    reload();
  }

  /**
   * Gets the header value (blocking)
   *
   * <p>NOTE: throws {@link ConfigurationException} if the response is 401, and {@link
   * com.sinch.sdk.exception.ApiException} for other http errors
   *
   * @return header value to use with @{HEADER_KEY_AUTH}
   */
  @SneakyThrows
  public String getHeaderValue() {
    return authHeaderFuture.get();
  }

  private void reload() {
    authHeaderFuture =
        CompletableFuture.supplyAsync(this::getAuthResponse)
            .thenApply(
                authResponse -> {
                  scheduleReload(Math.max(authResponse.getExpiresIn() - 60, fallbackRetryDelay));
                  return String.format(TEMPLATE_BEARER_AUTH, authResponse.getAccessToken());
                });
  }

  @SneakyThrows
  private AuthResponse getAuthResponse() {
    final HttpResponse<InputStream> response =
        httpClient.send(bearerTokenRequest, HttpResponse.BodyHandlers.ofInputStream());
    if (response.statusCode() == 401) {
      throw new ConfigurationException(
          "Invalid credentials, verify the keyId and keySecret",
          ExceptionUtils.getResponseBody(response));
    }
    return objectMapper.readValue(SinchRestClient.validate(response).body(), AuthResponse.class);
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
