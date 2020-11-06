package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.app.App;
import java.util.List;
import javax.validation.Valid;

public interface AppService {

  App createApp(@Valid App app);

  List<App> listApps();

  App updateApp(@Valid App app, String appId);

  void deleteApp(String appId);

  App getApp(String appId);
}
