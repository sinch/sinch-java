package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.AppService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.app.App;
import com.sinch.sdk.model.conversationapi.app.service.ListAppsResponse;
import java.util.List;
import java.util.function.Supplier;
import javax.validation.Valid;

public class AppServiceImpl extends ConversationApiService implements AppService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s/apps";

  public AppServiceImpl(ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public App createApp(@Valid App app) {
    return postRequest("", App.class, app);
  }

  @Override
  public List<App> listApps() {
    return getRequest("", ListAppsResponse.class).getApps();
  }

  @Override
  public App updateApp(@Valid App app, String appId) {
    return patchRequest("/".concat(appId), App.class, app);
  }

  @Override
  public void deleteApp(String appId) {
    deleteRequest("/".concat(appId));
  }

  @Override
  public App getApp(String appId) {
    return getRequest("/".concat(appId), App.class);
  }
}
