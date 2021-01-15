package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.TypeApp;
import com.sinch.sdk.model.conversationapi.TypeWebhook;
import com.sinch.sdk.model.conversationapi.V1ListAppsResponse;
import com.sinch.sdk.model.conversationapi.V1ListWebhooksResponse;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AppService extends AbstractService {

  static final String PARAM_APP_ID = "appId";
  private static final String PARAM_APP = "app";

  public AppService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "apps";
  }

  /**
   * Creates an app (blocking)
   *
   * @param app The app to create. (required)
   * @return {@link TypeApp}
   * @throws ApiException if fails to make API call
   */
  public TypeApp create(final TypeApp app) throws ApiException {
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
   * @return Async task providing a {@link TypeApp}
   */
  public CompletableFuture<TypeApp> createAsync(final TypeApp app) {
    if (app == null) {
      return ExceptionUtils.missingParam(PARAM_APP);
    }
    return restClient.post(serviceURI, TypeApp.class, app);
  }

  /**
   * Delete an app (blocking)
   *
   * @param appId The ID of the app. (required)
   * @throws ApiException if fails to make API call
   */
  public void delete(final String appId) throws ApiException {
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
   * @return {@link TypeApp}
   * @throws ApiException if fails to make API call
   */
  public TypeApp get(final String appId) throws ApiException {
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
   * @return Async task providing a {@link TypeApp}
   */
  public CompletableFuture<TypeApp> getAsync(final String appId) {
    if (StringUtils.isEmpty(appId)) {
      return ExceptionUtils.missingParam(PARAM_APP_ID);
    }
    return restClient.get(withPath(appId), TypeApp.class);
  }

  /**
   * List all apps for a given project (blocking)
   *
   * @return List of {@link TypeApp}
   * @throws ApiException if fails to make API call
   */
  public List<TypeApp> list() throws ApiException {
    try {
      return listAsync().join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * List all apps for a given project
   *
   * @return Async task generating a list of {@link TypeApp}
   */
  public CompletableFuture<List<TypeApp>> listAsync() {
    return restClient
        .get(serviceURI, V1ListAppsResponse.class)
        .thenApply(V1ListAppsResponse::getApps);
  }

  /**
   * Update an app (blocking)
   *
   * @param appId The ID of the app. (required)
   * @param app The updated app. (required)
   * @return {@link TypeApp}
   * @throws ApiException if fails to make API call
   */
  public TypeApp update(final String appId, final TypeApp app) throws ApiException {
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
   * @return Async task providing a {@link TypeApp}
   */
  public CompletableFuture<TypeApp> updateAsync(final String appId, final TypeApp app) {
    if (StringUtils.isEmpty(appId)) {
      return ExceptionUtils.missingParam(PARAM_APP_ID);
    }
    if (app == null) {
      return ExceptionUtils.missingParam(PARAM_APP);
    }
    return restClient.patch(withPath(appId), TypeApp.class, app);
  }

  /**
   * List all webhooks for a given app (blocking)
   *
   * @param appId The ID of the App to list webhooks for. (required)
   * @return List of {@link TypeWebhook}
   * @throws ApiException if fails to make API call
   */
  public List<TypeWebhook> listWebhooks(final String appId) throws ApiException {
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
   * @return Async task generating a list of {@link TypeWebhook}
   */
  public CompletableFuture<List<TypeWebhook>> listWebhooksAsync(final String appId) {
    if (StringUtils.isEmpty(appId)) {
      return ExceptionUtils.missingParam(PARAM_APP_ID);
    }
    return restClient
        .get(withPath(appId.concat("/webhooks")), V1ListWebhooksResponse.class)
        .thenApply(V1ListWebhooksResponse::getWebhooks);
  }
}
