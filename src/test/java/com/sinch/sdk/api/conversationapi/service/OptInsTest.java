package com.sinch.sdk.api.conversationapi.service;

import static java.util.Collections.emptyList;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.*;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OptInsTest extends BaseConvIntegrationTest {

  private final String contactId = "your-contact-id";
  private final String appId = "your-app-id";

  private static OptIns optIns;

  @BeforeAll
  static void beforeAll() {
    optIns =
        Sinch.conversationApi(Region.EU, () -> new OkHttpRestClientFactory(new OkHttpClient()))
            .optIns();
  }

  @Test
  void testRegisterOptIn() {
    final OptInResponse response =
        optIns.optIn(
            new OptIn()
                .appId(appId)
                .addChannelsItem(ConversationChannel.WHATSAPP)
                .recipient(new Recipient().contactId(contactId)),
            null);
    prettyPrint(response);
  }

  @Test
  void testRegisterOptOut() {
    final OptOutResponse response =
        optIns.optOut(
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
        Assertions.assertThrows(ApiException.class, () -> optIns.optIn(null, null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optIns.optIn(new OptIn().appId(appId), null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optIns.optIn(new OptIn().channels(emptyList()), null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> optIns.optOut(null, null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optIns.optOut(new OptOut().appId(appId), null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> optIns.optOut(new OptOut().channels(emptyList()), null));
    assertClientSideException(exception);
  }
}
