package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.AppEvent;
import com.sinch.sdk.model.conversationapi.Recipient;
import com.sinch.sdk.model.conversationapi.V1SendEventRequest;
import com.sinch.sdk.model.conversationapi.V1SendEventResponse;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EventServiceTest extends BaseConvIntegrationTest {

  private final String contactId = "your-contact-id";
  private final String appId = "your-app-id";

  private static EventService eventService;

  @BeforeAll
  static void beforeAll() {
    eventService = Sinch.conversationApi(Region.EU).events();
  }

  @Test
  void testSendEvent() throws ApiException {
    final V1SendEventResponse response =
        eventService.send(
            new V1SendEventRequest()
                .appId(appId)
                .event(new AppEvent().composingEvent(Map.of("a", "b")))
                .recipient(new Recipient().contactId(contactId)));
    prettyPrint(response);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> eventService.send(null));
    assertClientSideException(exception);
  }
}
