package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OptInServiceTest extends BaseConvIntegrationTest {

  private final String contactId = "your-contact-id";
  private final String appId = "your-app-id";

  private static OptInService optInService;

  @BeforeAll
  static void beforeAll() {
    optInService = Sinch.conversationApi(Region.EU).optIns();
  }

  @Test
  void testRegisterOptIn() throws ApiException {
    final V1OptInResponse response =
        optInService.optIn(
            new OptIn()
                .appId(appId)
                .addChannelsItem(ConversationChannel.WHATSAPP)
                .recipient(new Recipient().contactId(contactId)),
            null);
    prettyPrint(response);
  }

  @Test
  void testRegisterOptOut() throws ApiException {
    final V1OptOutResponse response =
        optInService.optOut(
            new OptOut()
                .appId(appId)
                .addChannelsItem(ConversationChannel.WHATSAPP)
                .recipient(new Recipient().contactId(contactId)),
            null);
    prettyPrint(response);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> optInService.optIn(null, null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optInService.optIn(new OptIn().appId(appId), null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optInService.optIn(new OptIn().channels(List.of()), null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> optInService.optOut(null, null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optInService.optOut(new OptOut().appId(appId), null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optInService.optOut(new OptOut().channels(List.of()), null));
    assertClientSideException(exception);
  }
}
