package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.TypeWebhook;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class WebhookService extends AbstractService {

  private static final String PARAM_WEBHOOK_ID = "webhookId";
  private static final String PARAM_WEBHOOK = "webhook";
  private static final String PARAM_WEBHOOK_APP_ID = PARAM_WEBHOOK + SUB_APP_ID;
  private static final String PARAM_WEBHOOK_TARGET = PARAM_WEBHOOK + ".target";

  private final AppService appService;

  public WebhookService(final ConversationApiConfig config) {
    super(config);
    appService = new AppService(config);
  }

  @Override
  protected String getServiceName() {
    return "webhooks";
  }

  /**
   * Create a new webhook (blocking)
   *
   * <p>Creates a webhook for receiving callbacks on specific triggers. You can create up to 20
   * webhooks per app.
   *
   * @param webhook The Webhook to create (required)
   * @return {@link TypeWebhook}
   * @throws ApiException if fails to make API call
   */
  public TypeWebhook create(final TypeWebhook webhook) throws ApiException {
    try {
      return createAsync(webhook).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Create a new webhook
   *
   * <p>Creates a webhook for receiving callbacks on specific triggers. You can create up to 20
   * webhooks per app.
   *
   * @param webhook The Webhook to create (required)
   * @return Async task generating a {@link TypeWebhook}
   */
  public CompletableFuture<TypeWebhook> createAsync(final TypeWebhook webhook) {
    if (webhook == null) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK);
    }
    if (StringUtils.isEmpty(webhook.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_APP_ID);
    }
    if (webhook.getTarget() == null) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_TARGET);
    }
    return restClient.post(serviceURI, TypeWebhook.class, webhook);
  }

  /**
   * Delete an existing webhook (blocking)
   *
   * @param webhookId The ID of the webhook to delete. (required)
   * @throws ApiException if fails to make API call
   */
  public void delete(final String webhookId) throws ApiException {
    try {
      deleteAsync(webhookId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Delete an existing webhook
   *
   * @param webhookId The ID of the webhook to delete. (required)
   * @return Async task of the delete call
   */
  public CompletableFuture<Void> deleteAsync(final String webhookId) {
    if (StringUtils.isEmpty(webhookId)) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_ID);
    }
    return restClient.delete(withPath(webhookId));
  }

  /**
   * Get a webhook (blocking)
   *
   * @param webhookId The ID of the webhook to fetch. (required)
   * @return {@link TypeWebhook}
   * @throws ApiException if fails to make API call
   */
  public TypeWebhook get(final String webhookId) throws ApiException {
    try {
      return getAsync(webhookId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Get a webhook
   *
   * @param webhookId The ID of the webhook to fetch. (required)
   * @return Async task generating a {@link TypeWebhook}
   */
  public CompletableFuture<TypeWebhook> getAsync(final String webhookId) {
    if (StringUtils.isEmpty(webhookId)) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_ID);
    }
    return restClient.get(withPath(webhookId), TypeWebhook.class);
  }

  /**
   * List all webhooks for a given app (blocking)
   *
   * @param appId The ID of the App to list webhooks for. (required)
   * @return List of {@link TypeWebhook}
   * @throws ApiException if fails to make API call
   */
  public List<TypeWebhook> list(final String appId) throws ApiException {
    return appService.listWebhooks(appId);
  }

  /**
   * List all webhooks for a given app
   *
   * @param appId The ID of the App to list webhooks for. (required)
   * @return Async task generating a list of {@link TypeWebhook}
   */
  public CompletableFuture<List<TypeWebhook>> listAsync(final String appId) {
    return appService.listWebhooksAsync(appId);
  }

  /**
   * Update an existing webhook (blocking)
   *
   * @param webhookId The ID of the webhook. (required)
   * @param webhook The Webhook to update (required)
   * @return {@link TypeWebhook}
   * @throws ApiException if fails to make API call
   */
  public TypeWebhook update(final String webhookId, final TypeWebhook webhook) throws ApiException {
    try {
      return updateAsync(webhookId, webhook).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Update an existing webhook
   *
   * @param webhookId The ID of the webhook. (required)
   * @param webhook The Webhook to update (required)
   * @return Async task generating a {@link TypeWebhook}
   */
  public CompletableFuture<TypeWebhook> updateAsync(
      final String webhookId, final TypeWebhook webhook) {
    if (StringUtils.isEmpty(webhookId)) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_ID);
    }
    if (webhook == null) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK);
    }
    return restClient.patch(withPath(webhookId), TypeWebhook.class, webhook);
  }
}
