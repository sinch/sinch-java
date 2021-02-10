package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.App;
import com.sinch.sdk.model.conversationapi.Webhook;
import com.sinch.sdk.model.conversationapi.WebhookTargetType;
import com.sinch.sdk.model.conversationapi.WebhookTrigger;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WebhookServiceTest extends BaseConvIntegrationTest {

  private final String webhookUrl = "https://webhook.site/d9cb2b5f-5ecd-4c19-ac34-b059b6e5eae1";

  private static AppService appService;
  private static WebhookService webhookService;

  private static App app;

  @BeforeAll
  static void beforeAll() {
    final ConversationApiClient conversationApi = Sinch.conversationApi(Region.EU);
    appService = conversationApi.apps();
    webhookService = conversationApi.webhooks();
    app = appService.create(new App().displayName("Webhook test app"));
  }

  @AfterAll
  static void afterAll() {
    appService.delete(app.getId());
  }

  @Test
  void testCreateWebhook() {
    final Webhook webhook =
        webhookService.create(
            new Webhook()
                .appId(app.getId())
                .target(webhookUrl)
                .addTriggersItem(WebhookTrigger.CONTACT_CREATE)
                .addTriggersItem(WebhookTrigger.CAPABILITY)
                .addTriggersItem(WebhookTrigger.OPT_IN)
                .addTriggersItem(WebhookTrigger.OPT_OUT)
                .addTriggersItem(WebhookTrigger.MESSAGE_DELIVERY)
                .targetType(WebhookTargetType.HTTP));

    Assertions.assertEquals(webhookUrl, webhook.getTarget());
    Assertions.assertNotNull(webhook.getTriggers());
    Assertions.assertEquals(5, webhook.getTriggers().size());
    Assertions.assertEquals(WebhookTargetType.HTTP, webhook.getTargetType());
    prettyPrint(webhook);
    webhookService.delete(webhook.getId());
  }

  @Test
  void deleteWebhook() {
    final Webhook webhook = createWebhook();
    webhookService.delete(webhook.getId());
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> webhookService.get(webhook.getId()));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
    Assertions.assertEquals(
        Optional.of("404"), exception.getResponseHeaders().firstValue(":status"));
  }

  @Test
  void getWebhook() {
    final Webhook webhook = webhookService.get(createWebhook().getId());
    prettyPrint(webhook);
    webhookService.delete(webhook.getId());
  }

  @Test
  void listWebhooks() {
    final List<Webhook> response = webhookService.list(app.getId());
    prettyPrint(response);
  }

  @Test
  void updateWebhook() {
    final Webhook update_webhook = createWebhook();
    final String expected_target = "https://www.google.com/";
    final Webhook webhook =
        webhookService.update(
            update_webhook.getId(),
            new Webhook().target(expected_target).targetType(WebhookTargetType.GRPC));
    Assertions.assertEquals(expected_target, webhook.getTarget());
    prettyPrint(webhook);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> webhookService.create(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> webhookService.create(new Webhook().appId("123")));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> webhookService.create(new Webhook().target("123")));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> webhookService.delete(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> webhookService.get(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> webhookService.list(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> webhookService.update(null, new Webhook()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> webhookService.update("123", null));
    assertClientSideException(exception);
  }

  private Webhook createWebhook() {
    return webhookService.create(new Webhook().appId(app.getId()).target(webhookUrl));
  }
}
