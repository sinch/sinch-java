package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

import com.sinch.sdk.model.conversationapi.App;
import com.sinch.sdk.model.conversationapi.ListAppsResponse;
import com.sinch.sdk.model.conversationapi.ListWebhooksResponse;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class AppsTest extends BaseServiceTest {

  private static final String APP_ID = "app-id";

  private static Apps apps;

  @BeforeEach
  void setUp() {
    apps = new Apps(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final Apps apps = new Apps(CONFIG, null);
    assertThat(apps.restClient).isNotNull();
    assertThat(apps.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, apps.getServiceName()));
  }

  @Test
  void createApp() {
    apps.create(new App());

    verifyPostCalled(() -> eq(apps.serviceURI), App.class);
  }

  @Test
  void deleteApp() {
    apps.delete(APP_ID);

    verifyDeleteCalled(() -> uriPathEndsWithMatcher(APP_ID));
  }

  @Test
  void getApp() {
    apps.get(APP_ID);

    verifyGetCalled(() -> uriPathEndsWithMatcher(APP_ID), App.class);
  }

  @Test
  void listApps() {
    apps.list();

    verifyGetCalled(() -> eq(apps.serviceURI), ListAppsResponse.class);
  }

  @Test
  void listAppThrows() {
    givenGetThrows();

    //noinspection ThrowableNotThrown
    verifyThrowsApiException(() -> apps.list());
  }

  @Test
  void updateApp() {
    apps.update(APP_ID, new App());

    verifyPatchCalled(() -> uriPathEndsWithMatcher(APP_ID), App.class);
  }

  @Test
  void listWebhooks() {
    apps.listWebhooks(APP_ID);

    verifyGetCalled(() -> uriPathEndsWithMatcher(APP_ID + "/webhooks"), ListWebhooksResponse.class);
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(final ThrowableAssert.ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowableAssert.ThrowingCallable> callsWithMissingParams() {
    return Arrays.asList(
        () -> apps.create(null),
        () -> apps.delete(null),
        () -> apps.get(null),
        () -> apps.update(null, new App()),
        () -> apps.update("appId", null),
        () -> apps.listWebhooks(null));
  }
}
