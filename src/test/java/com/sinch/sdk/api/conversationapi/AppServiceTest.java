package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.app.App;
import com.sinch.sdk.model.conversationapi.common.ConversationChannelCredential;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AppServiceTest extends BaseConvIntegrationTest {

  private final String appId = "your-app-id";

  private static AppService appService;

  @BeforeAll
  static void beforeAll() {
    appService = Sinch.conversationApi(Region.EU).getAppService();
  }

  @Test
  void testCreateApp() {
    final ConversationChannelCredential cred =
        ConversationChannelCredential.fromStaticToken()
            .channel(ConversationChannel.MESSENGER)
            .staticToken(ConversationChannelCredential.StaticToken.builder().token("token").build())
            .build();

    final App testApp =
        App.builder()
            .displayName("SDK test")
            .channelCredentials(List.of(cred))
            .retentionPolicy(
                App.RetentionPolicy.builder()
                    .retentionType(App.RetentionPolicyType.PERSIST_RETENTION_POLICY)
                    .build())
            .build();

    final App responseApp = appService.createApp(testApp);
    prettyPrint(responseApp);
  }

  @Test
  void testListApps() {
    final List<App> apps = appService.listApps();
    prettyPrint(apps);
  }

  @Test
  void testUpdateApp() {
    final App testApp =
        App.builder()
            .displayName("SDK test update")
            .retentionPolicy(
                App.RetentionPolicy.builder()
                    .retentionType(App.RetentionPolicyType.MESSAGE_EXPIRE_POLICY)
                    .build())
            .build();

    final App responseApp = appService.updateApp(testApp, appId);
    prettyPrint(responseApp);
  }

  @Test
  void testDeleteApp() {
    appService.deleteApp(appId);
    final App responseApp = appService.getApp(appId);
    prettyPrint(responseApp);
  }

  @Test
  void testGetApp() {
    final App responseApp = appService.getApp(appId);
    prettyPrint(responseApp);
  }
}
