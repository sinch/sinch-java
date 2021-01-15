package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AppServiceTest extends BaseConvIntegrationTest {

  private static AppService appService;

  @BeforeAll
  static void beforeAll() {
    appService = Sinch.conversationApi(Region.EU).apps();
  }

  @Test
  void testCreateApp() throws ApiException {
    final String displayName = "SDK test";
    final TypeApp app =
        appService.create(
            new TypeApp()
                .displayName(displayName)
                .addChannelCredentialsItem(
                    new TypeConversationChannelCredential()
                        .channel(TypeConversationChannel.MESSENGER)
                        .staticToken(new TypeStaticTokenCredential().token("token")))
                .retentionPolicy(
                    new TypeRetentionPolicy()
                        .retentionType(TypeRetentionPolicyType.PERSIST_RETENTION_POLICY)));

    Assertions.assertEquals(displayName, app.getDisplayName());
    Assertions.assertNotNull(app.getRetentionPolicy());
    Assertions.assertEquals(
        TypeRetentionPolicyType.PERSIST_RETENTION_POLICY,
        app.getRetentionPolicy().getRetentionType());
    prettyPrint(app);
    appService.delete(app.getId());
  }

  @Test
  void testDeleteApp() throws ApiException {
    final TypeApp app = createApp("To be deleted");
    appService.delete(app.getId());
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> appService.get(app.getId()));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
    Assertions.assertEquals(
        Optional.of("404"), exception.getResponseHeaders().firstValue(":status"));
  }

  @Test
  void testGetApp() throws ApiException {
    final TypeApp createdApp = createApp("Get app");
    final TypeApp app = appService.get(createdApp.getId());
    prettyPrint(app);
    appService.delete(app.getId());
  }

  @Test
  void testListApps() throws ApiException {
    final List<TypeApp> apps = appService.list();
    prettyPrint(apps);
  }

  @Test
  void testUpdateApp() throws ApiException {
    final TypeApp update_app = createApp("Update app");
    final String displayName = "Has been updated app";
    final TypeApp app =
        appService.update(update_app.getId(), new TypeApp().displayName(displayName));
    Assertions.assertEquals(displayName, app.getDisplayName());
    prettyPrint(app);
    appService.delete(app.getId());
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> appService.create(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> appService.delete(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> appService.get(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> appService.update(null, new TypeApp()));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> appService.update("123", null));
    assertClientSideException(exception);
  }

  private TypeApp createApp(final String displayName) throws ApiException {
    return appService.create(new TypeApp().displayName(displayName));
  }
}
