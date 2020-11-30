package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.event.ConversationEvent.AppEvent;
import com.sinch.sdk.model.conversationapi.event.ConversationEvent.ComposingEvent;
import com.sinch.sdk.model.conversationapi.event.service.SendEventRequest;
import com.sinch.sdk.model.conversationapi.event.service.SendEventResponse;
import org.junit.jupiter.api.Test;

public class TestEvent extends AbstractTest {
  private static final String contactId = "your-contact-id";
  private static final String appId = "your-app-id";

  @Test
  public void testSendEvent() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    SendEventRequest request =
        SendEventRequest.builder()
            .appId(appId)
            .event(AppEvent.builder().composingEvent(ComposingEvent.builder().build()).build())
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    SendEventResponse response = client.getEventService().sendEvent(request);
    System.out.println(response);
  }
}
