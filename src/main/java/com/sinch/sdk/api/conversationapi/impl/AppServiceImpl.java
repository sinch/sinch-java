package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.AppService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.app.App;
import com.sinch.sdk.model.conversationapi.app.service.ListAppsResponse;
import java.util.List;
import javax.validation.Valid;

public class AppServiceImpl extends ConversationApiService implements AppService {

  public AppServiceImpl(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "apps";
  }

  @Override
  public App createApp(@Valid final App app) {
    return restClient.post(serviceURI, App.class, app);
  }

  @Override
  public List<App> listApps() {
    return restClient.get(serviceURI, ListAppsResponse.class).getApps();
  }

  @Override
  public App updateApp(@Valid final App app, final String appId) {
    return restClient.patch(withPath(appId), App.class, app);
  }

  @Override
  public void deleteApp(final String appId) {
    restClient.delete(withPath(appId));
  }

  @Override
  public App getApp(final String appId) {
    return restClient.get(withPath(appId), App.class);
  }
}
