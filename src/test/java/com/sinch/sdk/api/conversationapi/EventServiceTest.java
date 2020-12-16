package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.BaseTest;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.event.ConversationEvent;
import com.sinch.sdk.model.conversationapi.event.service.SendEventRequest;
import com.sinch.sdk.model.conversationapi.event.service.SendEventResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EventServiceTest extends BaseTest {

  private final String contactId = "your-contact-id";
  private final String appId = "your-app-id";

  private static EventService eventService;

  @BeforeAll
  static void beforeAll() {
    eventService = Sinch.conversationApi(Region.EU).getEventService();
  }

  @Test
  void testSendEvent() {
    final SendEventRequest request =
        SendEventRequest.builder()
            .appId(appId)
            .event(
                ConversationEvent.AppEvent.builder()
                    .composingEvent(ConversationEvent.ComposingEvent.builder().build())
                    .build())
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    final SendEventResponse response = eventService.sendEvent(request);
    prettyPrint(response);
  }
}
