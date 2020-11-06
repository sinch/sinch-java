package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.WebhookService;
import com.sinch.sdk.model.conversationapi.webhook.Webhook;
import com.sinch.sdk.model.conversationapi.webhook.service.ListWebhooksResponse;
import java.util.List;
import java.util.function.Supplier;
import javax.validation.Valid;

public class WebhookServiceImpl extends ConversationApiService implements WebhookService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s";
  private static final String WEBHOOKS_PATH = "/webhooks";
  private static final String APPS_PATH = "/apps/%s/webhooks";

  public WebhookServiceImpl(ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public Webhook createWebhook(@Valid Webhook webhook) {
    return postRequest(WEBHOOKS_PATH, Webhook.class, webhook);
  }

  @Override
  public List<Webhook> listWebhooks(String appId) {
    return getRequest(String.format(APPS_PATH, appId), ListWebhooksResponse.class).getWebhooks();
  }

  @Override
  public Webhook updateWebhook(@Valid Webhook webhook, String webhookId) {
    return patchRequest(WEBHOOKS_PATH.concat("/").concat(webhookId), Webhook.class, webhook);
  }

  @Override
  public void deleteWebhook(String webhookId) {
    deleteRequest(WEBHOOKS_PATH.concat("/").concat(webhookId));
  }

  @Override
  public Webhook getWebhook(String webhookId) {
    return getRequest(WEBHOOKS_PATH.concat("/").concat(webhookId), Webhook.class);
  }
}
