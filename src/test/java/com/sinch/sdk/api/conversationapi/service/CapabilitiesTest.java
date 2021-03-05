package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.QueryCapabilityResponse;
import com.sinch.sdk.model.conversationapi.Recipient;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CapabilitiesTest extends BaseConvIntegrationTest {

  private static Capabilities capabilities;

  @BeforeAll
  static void beforeAll() {
    capabilities =
        Sinch.conversationApi(Region.EU, () -> new OkHttpRestClientFactory(new OkHttpClient()))
            .capabilities();
  }

  @Test
  void testQueryCapability() {
    final QueryCapabilityResponse response =
        capabilities.query(
            new QueryCapabilityRequest()
                .appId("your-app-id")
                .recipient(new Recipient().contactId("your-contact-id")));

    prettyPrint(response);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> capabilities.query(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> capabilities.query(new QueryCapabilityRequest()));
    assertClientSideException(exception);
  }
}
