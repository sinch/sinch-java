package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AppsTest extends BaseConvIntegrationTest {

  private static Apps apps;

  @BeforeAll
  static void beforeAll() {
    apps = Sinch.conversationApi(Region.EU).apps();
  }

  @Test
  void testCreateApp() {
    final String displayName = "SDK test";
    final App app =
        apps.create(
            new App()
                .displayName(displayName)
                .addChannelCredentialsItem(
                    new ConversationChannelCredential()
                        .channel(ConversationChannel.MESSENGER)
                        .staticToken(new StaticTokenCredential().token("token")))
                .retentionPolicy(
                    new RetentionPolicy()
                        .retentionType(RetentionPolicyType.PERSIST_RETENTION_POLICY)));

    Assertions.assertEquals(displayName, app.getDisplayName());
    Assertions.assertNotNull(app.getRetentionPolicy());
    Assertions.assertEquals(
        RetentionPolicyType.PERSIST_RETENTION_POLICY, app.getRetentionPolicy().getRetentionType());
    prettyPrint(app);
    apps.delete(app.getId());
  }

  @Test
  void testDeleteApp() {
    final App app = createApp("To be deleted");
    apps.delete(app.getId());
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> apps.get(app.getId()));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
  }

  @Test
  void testGetApp() {
    final App createdApp = createApp("Get app");
    final App app = apps.get(createdApp.getId());
    prettyPrint(app);
    apps.delete(app.getId());
  }

  @Test
  void testListApps() {
    final List<App> apps = AppsTest.apps.list();
    prettyPrint(apps);
  }

  @Test
  void testUpdateApp() {
    final App update_app = createApp("Update app");
    final String displayName = "Has been updated app";
    final App app = apps.update(update_app.getId(), new App().displayName(displayName));
    Assertions.assertEquals(displayName, app.getDisplayName());
    prettyPrint(app);
    apps.delete(app.getId());
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception = Assertions.assertThrows(ApiException.class, () -> apps.create(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> apps.delete(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> apps.get(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> apps.update(null, new App()));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> apps.update("123", null));
    assertClientSideException(exception);
  }

  private App createApp(final String displayName) {
    return apps.create(new App().displayName(displayName));
  }
}
