package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.app.App;
import com.sinch.sdk.model.conversationapi.app.App.RetentionPolicy;
import com.sinch.sdk.model.conversationapi.app.App.RetentionPolicyType;
import com.sinch.sdk.model.conversationapi.common.ConversationChannelCredential;
import com.sinch.sdk.model.conversationapi.common.ConversationChannelCredential.StaticToken;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestApp extends AbstractTest{

  private static final String appId = "your-app-id";

  @Test
  public void testListApps() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    List<App> apps = client.getAppService().listApps();
    System.out.println(apps);
  }

  @Test
  public void testCreateApp() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    ConversationChannelCredential cred =
        ConversationChannelCredential.fromStaticToken()
            .channel(ConversationChannel.MESSENGER)
            .staticToken(StaticToken.builder().token("token").build())
            .build();

    App testApp =
        App.builder()
            .displayName("SDK test")
            .channelCredentials(List.of(cred))
            .retentionPolicy(
                RetentionPolicy.builder()
                    .retentionType(RetentionPolicyType.PERSIST_RETENTION_POLICY)
                    .build())
            .build();

    App responseApp = client.getAppService().createApp(testApp);
    System.out.println(responseApp);
  }

  @Test
  public void testGetApp() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    App responseApp = client.getAppService().getApp(appId);

    System.out.println(responseApp);
  }

  @Test
  public void testDeleteApp() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    client.getAppService().deleteApp(appId);

    App responseApp = client.getAppService().getApp(appId);

    System.out.println(responseApp);
  }

  @Test
  public void testUpdateApp() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    App testApp =
        App.builder()
            .displayName("SDK test update")
            .retentionPolicy(
                RetentionPolicy.builder()
                    .retentionType(RetentionPolicyType.MESSAGE_EXPIRE_POLICY)
                    .build())
            .build();

    App responseApp = client.getAppService().updateApp(testApp, appId);
    System.out.println(responseApp);
  }
}
