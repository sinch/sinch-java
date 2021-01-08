package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.app.App;
import com.sinch.sdk.model.conversationapi.app.service.ListAppsResponse;
import java.util.List;
import javax.validation.Valid;

public class AppService extends AbstractService {

  public AppService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "apps";
  }

  public App createApp(@Valid final App app) {
    return restClient.post(serviceURI, App.class, app);
  }

  public List<App> listApps() {
    return restClient.get(serviceURI, ListAppsResponse.class).getApps();
  }

  public App updateApp(@Valid final App app, final String appId) {
    return restClient.patch(withPath(appId), App.class, app);
  }

  public void deleteApp(final String appId) {
    restClient.delete(withPath(appId));
  }

  public App getApp(final String appId) {
    return restClient.get(withPath(appId), App.class);
  }
}
