package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MessageServiceTest extends BaseTest {

  private final String appId = "your-app-id";
  private final String contactId = "your-contact-id";
  private final String messageId = "your-message-id";

  private static MessageService messageService;

  @BeforeAll
  static void beforeAll() {
    messageService = Sinch.conversationApi().getMessageService();
  }

  @Test
  void testSendAppMessage() {
    final SendAppMessageRequest request =
        SendAppMessageRequest.builder()
            .appId(appId)
            .message(
                AppMessage.fromTextMessage()
                    .textMessage(TextMessage.builder().text("SDK text message").build())
                    .build())
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .channelPriorityOrder(List.of(ConversationChannel.VIBER))
            .build();

    final SendAppMessageResponse response = messageService.sendAppMessage(request);
    prettyPrint(response);
  }

  @Test
  void testGetMessage() {
    final ConversationMessage response = messageService.getMessage(messageId);
    prettyPrint(response);
  }

  @Test
  void testTranscodeMessage() {
    final TranscodeMessageRequest request =
        TranscodeMessageRequest.builder()
            .appMessage(
                AppMessage.fromTextMessage()
                    .textMessage(TextMessage.builder().text("SDK text message").build())
                    .build())
            .channels(List.of(ConversationChannel.VIBER))
            .appId(appId)
            .build();

    final TranscodeMessageResponse response = messageService.transcodeMessage(request);
    prettyPrint(response);
  }
}
