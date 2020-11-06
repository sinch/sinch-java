package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.webhook.Webhook;
import com.sinch.sdk.model.conversationapi.webhook.Webhook.TargetType;
import com.sinch.sdk.model.conversationapi.webhook.Webhook.Trigger;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestWebhook extends AbstractTest{
  private static final String webhookId = "your-webhook-id";
  private static final String appId = "your-app-id";

  @Test
  public void testCreateWebhook() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Webhook w =
        Webhook.builder()
            .appId(appId)
            .target("https://webhook.site/d9cb2b5f-5ecd-4c19-ac34-b059b6e5eae1")
            .triggers(
                List.of(
                    Trigger.CONTACT_CREATE,
                    Trigger.CAPABILITY,
                    Trigger.OPT_IN,
                    Trigger.OPT_OUT,
                    Trigger.MESSAGE_DELIVERY))
            .targetType(TargetType.HTTP)
            .build();

    Webhook response = client.getWebhookService().createWebhook(w);
    System.out.println(response);
  }

  @Test
  public void testUpdateWebhook() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Webhook w =
        Webhook.builder()
            .appId(appId)
            .target("https://webhook.site/d9cb2b5f-5ecd-4c19-ac34-b059b6e5eae1")
            .triggers(
                List.of(
                    Trigger.CONTACT_CREATE,
                    Trigger.CAPABILITY,
                    Trigger.OPT_IN,
                    Trigger.OPT_OUT,
                    Trigger.MESSAGE_DELIVERY))
            .targetType(TargetType.GRPC)
            .build();

    Webhook response = client.getWebhookService().updateWebhook(w, webhookId);
    System.out.println(response);
  }

  @Test
  public void testGetWebhook() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Webhook response = client.getWebhookService().getWebhook(webhookId);
    System.out.println(response);
  }

  @Test
  public void testListWebhook() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    List<Webhook> response = client.getWebhookService().listWebhooks(appId);
    System.out.println(response);
  }

  @Test
  public void testDeleteWebhook() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    client.getWebhookService().deleteWebhook(webhookId);

    Webhook response = client.getWebhookService().getWebhook(webhookId);
    System.out.println(response);
  }
}
