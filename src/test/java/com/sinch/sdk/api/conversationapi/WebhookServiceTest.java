package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.model.conversationapi.webhook.Webhook;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WebhookServiceTest extends BaseTest {

  private final String webhookId = "your-webhook-id";
  private final String appId = "your-app-id";

  private static WebhookService webhookService;

  @BeforeAll
  static void beforeAll() {
    webhookService = Sinch.conversationApi().getWebhookService();
  }

  @Test
  void testCreateWebhook() {
    final Webhook w =
        Webhook.builder()
            .appId(appId)
            .target("https://webhook.site/d9cb2b5f-5ecd-4c19-ac34-b059b6e5eae1")
            .triggers(
                List.of(
                    Webhook.Trigger.CONTACT_CREATE,
                    Webhook.Trigger.CAPABILITY,
                    Webhook.Trigger.OPT_IN,
                    Webhook.Trigger.OPT_OUT,
                    Webhook.Trigger.MESSAGE_DELIVERY))
            .targetType(Webhook.TargetType.HTTP)
            .build();

    Webhook response = webhookService.createWebhook(w);
    prettyPrint(response);
  }

  @Test
  void listWebhooks() {
    final List<Webhook> response = webhookService.listWebhooks(appId);
    prettyPrint(response);
  }

  @Test
  void updateWebhook() {
    final Webhook w =
        Webhook.builder()
            .appId(appId)
            .target("https://webhook.site/d9cb2b5f-5ecd-4c19-ac34-b059b6e5eae1")
            .triggers(
                List.of(
                    Webhook.Trigger.CONTACT_CREATE,
                    Webhook.Trigger.CAPABILITY,
                    Webhook.Trigger.OPT_IN,
                    Webhook.Trigger.OPT_OUT,
                    Webhook.Trigger.MESSAGE_DELIVERY))
            .targetType(Webhook.TargetType.GRPC)
            .build();

    Webhook response = webhookService.updateWebhook(w, webhookId);
    prettyPrint(response);
  }

  @Test
  void deleteWebhook() {
    webhookService.deleteWebhook(webhookId);
    final Webhook response = webhookService.getWebhook(webhookId);
    prettyPrint(response);
  }

  @Test
  void getWebhook() {
    final Webhook response = webhookService.getWebhook(webhookId);
    prettyPrint(response);
  }
}
