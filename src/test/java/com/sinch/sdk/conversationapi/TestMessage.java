package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.message.AppMessage;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageRequest;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageResponse;
import com.sinch.sdk.model.conversationapi.message.type.TextMessage;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestMessage extends AbstractTest{
  private static final String contactId = "your-contact-id";
  private static final String appId = "your-app-id";
  private static final String messageId = "your-message-id";

  @Test
  public void testSendMessage() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    SendAppMessageRequest request =
        SendAppMessageRequest.builder()
            .appId(appId)
            .message(
                AppMessage.fromTextMessage()
                    .textMessage(TextMessage.builder().text("SDK text message").build())
                    .build())
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .channelPriorityOrder(List.of(ConversationChannel.VIBER))
            .build();

    SendAppMessageResponse response = client.getMessageService().sendAppMessage(request);
    System.out.println(response);
  }

  @Test
  public void testGetMessage() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    ConversationMessage response =
        client.getMessageService().getMessage(messageId);
    System.out.println(response);
  }

  @Test
  public void testTranscodeMessage() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    TranscodeMessageRequest request =
        TranscodeMessageRequest.builder()
            .appMessage(
                AppMessage.fromTextMessage()
                    .textMessage(TextMessage.builder().text("SDK text message").build())
                    .build())
            .channels(List.of(ConversationChannel.VIBER))
            .appId(appId)
            .build();

    TranscodeMessageResponse response = client.getMessageService().transcodeMessage(request);
    System.out.println(response);
  }
}
