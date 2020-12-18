package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.WebhookService;
import com.sinch.sdk.model.conversationapi.webhook.Webhook;
import com.sinch.sdk.model.conversationapi.webhook.service.ListWebhooksResponse;
import java.util.List;
import javax.validation.Valid;

public class WebhookServiceImpl extends ConversationApiService implements WebhookService {

  private final String appService;

  public WebhookServiceImpl(final ConversationApiConfig config) {
    super(config);
    appService = String.format(TEMPLATE_URL, config.getBaseUrl(), version, projectId, "apps");
  }

  @Override
  protected String getServiceName() {
    return "webhooks";
  }

  @Override
  public Webhook createWebhook(@Valid final Webhook webhook) {
    return restClient.post(serviceURI, Webhook.class, webhook);
  }

  @Override
  public List<Webhook> listWebhooks(final String appId) {
    return restClient
        .get(withPath(appService, String.format("%s/webhooks", appId)), ListWebhooksResponse.class)
        .getWebhooks();
  }

  @Override
  public Webhook updateWebhook(@Valid final Webhook webhook, final String webhookId) {
    return restClient.patch(withPath(webhookId), Webhook.class, webhook);
  }

  @Override
  public void deleteWebhook(final String webhookId) {
    restClient.delete(withPath(webhookId));
  }

  @Override
  public Webhook getWebhook(final String webhookId) {
    return restClient.get(withPath(webhookId), Webhook.class);
  }
}
