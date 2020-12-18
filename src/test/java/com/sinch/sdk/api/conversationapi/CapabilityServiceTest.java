package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.BaseTest;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityResponse;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CapabilityServiceTest extends BaseTest {

  private final String appId = "your-app-id";
  private final String contactId = "your-contact-id";

  private static CapabilityService capabilityService;

  @BeforeAll
  static void beforeAll() {
    capabilityService = Sinch.conversationApi(Region.EU).getCapabilityService();
  }

  @Test
  void testQueryCapability() {
    final QueryCapabilityRequest rq =
        QueryCapabilityRequest.builder()
            .appId(appId)
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    QueryCapabilityResponse response = capabilityService.queryCapability(rq);
    prettyPrint(response);
  }
}
