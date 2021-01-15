package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.TypeApp;
import com.sinch.sdk.model.conversationapi.TypeWebhook;
import com.sinch.sdk.model.conversationapi.TypeWebhookTargetType;
import com.sinch.sdk.model.conversationapi.TypeWebhookTrigger;
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

  private static TypeApp app;

  @BeforeAll
  static void beforeAll() throws ApiException {
    final ConversationApiClient conversationApi = Sinch.conversationApi(Region.EU);
    appService = conversationApi.apps();
    webhookService = conversationApi.webhooks();
    app = appService.create(new TypeApp().displayName("Webhook test app"));
  }

  @AfterAll
  static void afterAll() throws ApiException {
    appService.delete(app.getId());
  }

  @Test
  void testCreateWebhook() throws ApiException {
    final TypeWebhook webhook =
        webhookService.create(
            new TypeWebhook()
                .appId(app.getId())
                .target(webhookUrl)
                .addTriggersItem(TypeWebhookTrigger.CONTACT_CREATE)
                .addTriggersItem(TypeWebhookTrigger.CAPABILITY)
                .addTriggersItem(TypeWebhookTrigger.OPT_IN)
                .addTriggersItem(TypeWebhookTrigger.OPT_OUT)
                .addTriggersItem(TypeWebhookTrigger.MESSAGE_DELIVERY)
                .targetType(TypeWebhookTargetType.HTTP));

    Assertions.assertEquals(webhookUrl, webhook.getTarget());
    Assertions.assertNotNull(webhook.getTriggers());
    Assertions.assertEquals(5, webhook.getTriggers().size());
    Assertions.assertEquals(TypeWebhookTargetType.HTTP, webhook.getTargetType());
    prettyPrint(webhook);
    webhookService.delete(webhook.getId());
  }

  @Test
  void deleteWebhook() throws ApiException {
    final TypeWebhook webhook = createWebhook();
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
  void getWebhook() throws ApiException {
    final TypeWebhook webhook = webhookService.get(createWebhook().getId());
    prettyPrint(webhook);
    webhookService.delete(webhook.getId());
  }

  @Test
  void listWebhooks() throws ApiException {
    final List<TypeWebhook> response = webhookService.list(app.getId());
    prettyPrint(response);
  }

  @Test
  void updateWebhook() throws ApiException {
    final TypeWebhook update_webhook = createWebhook();
    final String expected_target = "https://www.google.com/";
    final TypeWebhook webhook =
        webhookService.update(
            update_webhook.getId(),
            new TypeWebhook().target(expected_target).targetType(TypeWebhookTargetType.GRPC));
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
            ApiException.class, () -> webhookService.create(new TypeWebhook().appId("123")));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> webhookService.create(new TypeWebhook().target("123")));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> webhookService.delete(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> webhookService.get(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> webhookService.list(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> webhookService.update(null, new TypeWebhook()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> webhookService.update("123", null));
    assertClientSideException(exception);
  }

  private TypeWebhook createWebhook() throws ApiException {
    return webhookService.create(new TypeWebhook().appId(app.getId()).target(webhookUrl));
  }
}
