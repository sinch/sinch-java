package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

import com.sinch.sdk.model.conversationapi.Webhook;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class WebhooksTest extends BaseServiceTest {

  private static final String APP_ID = "app-id";
  private static final String WEBHOOK_ID = "webhook-id";
  private static final String WEBHOOK_URL = "webhook-url";

  private static Webhooks webhooks;

  @BeforeEach
  void setUp() {
    webhooks = new Webhooks(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final Webhooks webhooks = new Webhooks(CONFIG, null);
    assertThat(webhooks.restClient).isNotNull();
    assertThat(webhooks.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, webhooks.getServiceName()));
  }

  @Test
  void createWebhook() {
    webhooks.create(new Webhook().appId(APP_ID).target(WEBHOOK_URL));

    verifyPostCalled(() -> eq(webhooks.serviceURI), Webhook.class);
  }

  @Test
  void deleteWebhook() {
    webhooks.delete(WEBHOOK_ID);

    verifyDeleteCalled(() -> uriPathEndsWithMatcher(WEBHOOK_ID));
  }

  @Test
  void getWebhook() {

    webhooks.get(WEBHOOK_ID);

    verifyGetCalled(() -> uriPathEndsWithMatcher(WEBHOOK_ID), Webhook.class);
  }

  @Test
  void listWebhooks() {
    // For coverage, asserted in MessageTest
    webhooks.list(APP_ID);
    webhooks.listAsync(APP_ID);
  }

  @Test
  void updateWebhook() {
    webhooks.update(WEBHOOK_ID, new Webhook());

    verifyPatchCalled(() -> uriPathEndsWithMatcher(WEBHOOK_ID), Webhook.class);
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(final ThrowableAssert.ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowableAssert.ThrowingCallable> callsWithMissingParams() {
    return Arrays.asList(
        () -> webhooks.create(null),
        () -> webhooks.create(new Webhook().appId(APP_ID)),
        () -> webhooks.create(new Webhook().target(WEBHOOK_URL)),
        () -> webhooks.delete(null),
        () -> webhooks.get(null),
        () -> webhooks.list(null),
        () -> webhooks.update(null, new Webhook()),
        () -> webhooks.update(WEBHOOK_ID, null));
  }
}
