package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.AppEvent;
import com.sinch.sdk.model.conversationapi.Recipient;
import com.sinch.sdk.model.conversationapi.SendEventRequest;
import com.sinch.sdk.model.conversationapi.SendEventResponse;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EventsTest extends BaseConvIntegrationTest {

  private final String contactId = "your-contact-id";
  private final String appId = "your-app-id";

  private static Events events;

  @BeforeAll
  static void beforeAll() {
    events =
        Sinch.conversationApi(Region.EU, () -> new OkHttpRestClientFactory(new OkHttpClient()))
            .events();
  }

  @Test
  void testSendEvent() {
    final Map<String, String> composingEvent = new HashMap<>();
    composingEvent.put("a", "b");
    final SendEventResponse response =
        events.send(
            new SendEventRequest()
                .appId(appId)
                .event(new AppEvent().composingEvent(composingEvent))
                .recipient(new Recipient().contactId(contactId)));
    prettyPrint(response);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception = Assertions.assertThrows(ApiException.class, () -> events.send(null));
    assertClientSideException(exception);
  }
}
