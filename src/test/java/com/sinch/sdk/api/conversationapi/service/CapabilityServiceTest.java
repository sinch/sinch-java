package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.QueryCapabilityResponse;
import com.sinch.sdk.model.conversationapi.Recipient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CapabilityServiceTest extends BaseConvIntegrationTest {

  private static CapabilityService capabilityService;

  @BeforeAll
  static void beforeAll() {
    capabilityService = Sinch.conversationApi(Region.EU).capabilities();
  }

  @Test
  void testQueryCapability() {
    final QueryCapabilityResponse response =
        capabilityService.query(
            new QueryCapabilityRequest()
                .appId("your-app-id")
                .recipient(new Recipient().contactId("your-contact-id")));

    prettyPrint(response);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> capabilityService.query(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> capabilityService.query(new QueryCapabilityRequest()));
    assertClientSideException(exception);
  }
}
