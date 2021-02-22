package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.Webhook;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Webhooks extends AbstractService {

  private static final String PARAM_WEBHOOK_ID = "webhookId";
  private static final String PARAM_WEBHOOK = "webhook";
  private static final String PARAM_WEBHOOK_APP_ID = PARAM_WEBHOOK + SUB_APP_ID;
  private static final String PARAM_WEBHOOK_TARGET = PARAM_WEBHOOK + ".target";

  private final Apps apps;

  public Webhooks(
      final ConversationApiConfig config, final AuthenticationService authenticationService) {
    super(config, authenticationService);
    apps = new Apps(config, authenticationService);
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
   * @return {@link Webhook}
   * @throws ApiException if fails to make API call
   */
  public Webhook create(final Webhook webhook) {
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
   * @return Async task generating a {@link Webhook}
   */
  public CompletableFuture<Webhook> createAsync(final Webhook webhook) {
    if (webhook == null) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK);
    }
    if (StringUtils.isEmpty(webhook.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_APP_ID);
    }
    if (webhook.getTarget() == null) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_TARGET);
    }
    return restClient.post(serviceURI, Webhook.class, webhook);
  }

  /**
   * Delete an existing webhook (blocking)
   *
   * @param webhookId The ID of the webhook to delete. (required)
   * @throws ApiException if fails to make API call
   */
  public void delete(final String webhookId) {
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
   * @return {@link Webhook}
   * @throws ApiException if fails to make API call
   */
  public Webhook get(final String webhookId) {
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
   * @return Async task generating a {@link Webhook}
   */
  public CompletableFuture<Webhook> getAsync(final String webhookId) {
    if (StringUtils.isEmpty(webhookId)) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_ID);
    }
    return restClient.get(withPath(webhookId), Webhook.class);
  }

  /**
   * List all webhooks for a given app (blocking)
   *
   * @param appId The ID of the App to list webhooks for. (required)
   * @return List of {@link Webhook}
   * @throws ApiException if fails to make API call
   */
  public List<Webhook> list(final String appId) {
    return apps.listWebhooks(appId);
  }

  /**
   * List all webhooks for a given app
   *
   * @param appId The ID of the App to list webhooks for. (required)
   * @return Async task generating a list of {@link Webhook}
   */
  public CompletableFuture<List<Webhook>> listAsync(final String appId) {
    return apps.listWebhooksAsync(appId);
  }

  /**
   * Update an existing webhook (blocking)
   *
   * @param webhookId The ID of the webhook. (required)
   * @param webhook The Webhook to update (required)
   * @return {@link Webhook}
   * @throws ApiException if fails to make API call
   */
  public Webhook update(final String webhookId, final Webhook webhook) {
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
   * @return Async task generating a {@link Webhook}
   */
  public CompletableFuture<Webhook> updateAsync(final String webhookId, final Webhook webhook) {
    if (StringUtils.isEmpty(webhookId)) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK_ID);
    }
    if (webhook == null) {
      return ExceptionUtils.missingParam(PARAM_WEBHOOK);
    }
    return restClient.patch(withPath(webhookId), Webhook.class, webhook);
  }
}
