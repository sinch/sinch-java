package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.optin.OptIn;
import com.sinch.sdk.model.conversationapi.optin.OptOut;
import com.sinch.sdk.model.conversationapi.optin.service.OptInResponse;
import com.sinch.sdk.model.conversationapi.optin.service.OptOutResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OptInServiceTest extends BaseConvIntegrationTest {

  private final String contactId = "your-contact-id";
  private final String appId = "your-app-id";

  private static OptInService optInService;

  @BeforeAll
  static void beforeAll() {
    optInService = Sinch.conversationApi(Region.EU).getOptInService();
  }

  @Test
  void testRegisterOptIn() {
    final OptIn in =
        OptIn.builder()
            .appId(appId)
            .channels(List.of(ConversationChannel.WHATSAPP))
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    OptInResponse response = optInService.registerOptIn(in);
    prettyPrint(response);
  }

  @Test
  void testRegisterOptOut() {
    final OptOut out =
        OptOut.builder()
            .appId(appId)
            .channels(List.of(ConversationChannel.WHATSAPP))
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    OptOutResponse response = optInService.registerOptOut(out);
    prettyPrint(response);
  }
}
