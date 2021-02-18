package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.App;
import com.sinch.sdk.model.conversationapi.ListAppsResponse;
import com.sinch.sdk.model.conversationapi.ListWebhooksResponse;
import com.sinch.sdk.model.conversationapi.Webhook;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Apps extends AbstractService {

  static final String PARAM_APP_ID = "appId";
  private static final String PARAM_APP = "app";

  public Apps(
      final ConversationApiConfig config, final AuthenticationService authenticationService) {
    super(config, authenticationService);
  }

  @Override
  protected String getServiceName() {
    return "apps";
  }

  /**
   * Creates an app (blocking)
   *
   * @param app The app to create. (required)
   * @return {@link App}
   * @throws ApiException if fails to make API call
   */
  public App create(final App app) {
    try {
      return createAsync(app).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Creates an app
   *
   * @param app The app to create. (required)
   * @return Async task providing a {@link App}
   */
  public CompletableFuture<App> createAsync(final App app) {
    if (app == null) {
      return ExceptionUtils.missingParam(PARAM_APP);
    }
    return restClient.post(serviceURI, App.class, app);
  }

  /**
   * Delete an app (blocking)
   *
   * @param appId The ID of the app. (required)
   * @throws ApiException if fails to make API call
   */
  public void delete(final String appId) {
    try {
      deleteAsync(appId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Delete an app
   *
   * @param appId The ID of the app. (required)
   * @return Async task of the delete call
   */
  public CompletableFuture<Void> deleteAsync(final String appId) {
    if (StringUtils.isEmpty(appId)) {
      return ExceptionUtils.missingParam(PARAM_APP_ID);
    }
    return restClient.delete(withPath(appId));
  }

  /**
   * Get an app (blocking)
   *
   * @param appId The ID of the app to fetch. (required)
   * @return {@link App}
   * @throws ApiException if fails to make API call
   */
  public App get(final String appId) {
    try {
      return getAsync(appId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Get an app
   *
   * @param appId The ID of the app to fetch. (required)
   * @return Async task providing a {@link App}
   */
  public CompletableFuture<App> getAsync(final String appId) {
    if (StringUtils.isEmpty(appId)) {
      return ExceptionUtils.missingParam(PARAM_APP_ID);
    }
    return restClient.get(withPath(appId), App.class);
  }

  /**
   * List all apps for a given project (blocking)
   *
   * @return List of {@link App}
   * @throws ApiException if fails to make API call
   */
  public List<App> list() {
    try {
      return listAsync().join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * List all apps for a given project
   *
   * @return Async task generating a list of {@link App}
   */
  public CompletableFuture<List<App>> listAsync() {
    return restClient.get(serviceURI, ListAppsResponse.class).thenApply(ListAppsResponse::getApps);
  }

  /**
   * Update an app (blocking)
   *
   * @param appId The ID of the app. (required)
   * @param app The updated app. (required)
   * @return {@link App}
   * @throws ApiException if fails to make API call
   */
  public App update(final String appId, final App app) {
    try {
      return updateAsync(appId, app).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Update an app
   *
   * @param appId The ID of the app. (required)
   * @param app The updated app. (required)
   * @return Async task providing a {@link App}
   */
  public CompletableFuture<App> updateAsync(final String appId, final App app) {
    if (StringUtils.isEmpty(appId)) {
      return ExceptionUtils.missingParam(PARAM_APP_ID);
    }
    if (app == null) {
      return ExceptionUtils.missingParam(PARAM_APP);
    }
    return restClient.patch(withPath(appId), App.class, app);
  }

  /**
   * List all webhooks for a given app (blocking)
   *
   * @param appId The ID of the App to list webhooks for. (required)
   * @return List of {@link Webhook}
   * @throws ApiException if fails to make API call
   */
  public List<Webhook> listWebhooks(final String appId) {
    try {
      return listWebhooksAsync(appId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * List all webhooks for a given app
   *
   * @param appId The ID of the App to list webhooks for. (required)
   * @return Async task generating a list of {@link Webhook}
   */
  public CompletableFuture<List<Webhook>> listWebhooksAsync(final String appId) {
    if (StringUtils.isEmpty(appId)) {
      return ExceptionUtils.missingParam(PARAM_APP_ID);
    }
    return restClient
        .get(withPath(appId.concat("/webhooks")), ListWebhooksResponse.class)
        .thenApply(ListWebhooksResponse::getWebhooks);
  }
}
