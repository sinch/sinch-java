package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.webhook.Webhook;
import java.util.List;
import javax.validation.Valid;

public interface WebhookService {

  Webhook createWebhook(@Valid Webhook webhook);

  List<Webhook> listWebhooks(String appId);

  Webhook updateWebhook(@Valid Webhook webhook, String webhookId);

  void deleteWebhook(String webhookId);

  Webhook getWebhook(String webhookId);
}
