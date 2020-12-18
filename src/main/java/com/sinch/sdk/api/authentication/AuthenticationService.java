package com.sinch.sdk.api.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.model.common.auth.service.AuthResponse;
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

  private static final URI URL =
      URI.create("https://public.hydra.common-auth.staging.sinch.com/oauth2/token");

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final long fallbackRetryDelaySeconds;
  private final Timer refreshTimer;
  private final String basicHeaderValue;
  private final HttpRequest bearerTokenRequest;
  private CompletableFuture<String> authHeaderFuture;

  public AuthenticationService(
      final HttpClient httpClient,
      final ObjectMapper objectMapper,
      final long fallbackRetryDelaySeconds,
      @NonNull final String clientId,
      @NonNull final String clientSecret) {
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
    this.fallbackRetryDelaySeconds = fallbackRetryDelaySeconds;
    this.refreshTimer = new Timer("BearerTokenRefreshTimer");
    this.basicHeaderValue =
        String.format(
            TEMPLATE_BASIC_AUTH,
            Base64.getEncoder()
                .encodeToString(
                    String.format(TEMPLATE_ID_SECRET, clientId, clientSecret).getBytes()));
    this.bearerTokenRequest =
        HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
            .uri(URL)
            .header(HEADER_KEY_AUTH, basicHeaderValue)
            .header(HEADER_KEY_CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .timeout(Duration.ofSeconds(10))
            .build();
    reload();
  }

  /**
   * Retrieves Bearer token if possible and falls back to Basic token
   *
   * <p>Note: Blocking call
   *
   * @return header value to use with @{HEADER_KEY_AUTH}
   */
  @SneakyThrows
  public String getHeaderValue() {
    return authHeaderFuture.get();
  }

  /** Asynchronous call to request a new Bearer token */
  public void reload() {
    authHeaderFuture =
        CompletableFuture.supplyAsync(this::getAuthResponse)
            .handle(
                (authResponse, exception) -> {
                  if (authResponse != null) {
                    scheduleReload(authResponse.getExpiresIn());
                    return String.format(TEMPLATE_BEARER_AUTH, authResponse.getAccessToken());
                  }
                  scheduleReload(fallbackRetryDelaySeconds);
                  return basicHeaderValue;
                });
  }

  @SneakyThrows
  private AuthResponse getAuthResponse() {
    return objectMapper.readValue(
        httpClient.send(bearerTokenRequest, HttpResponse.BodyHandlers.ofInputStream()).body(),
        AuthResponse.class);
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
