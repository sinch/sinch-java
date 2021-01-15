package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MessageServiceTest extends BaseConvIntegrationTest {

  private final String appId = "your-app-id";
  private final String contactId = "your-contact-id";
  private final String messageId = "your-message-id";

  private static MessageService messageService;

  @BeforeAll
  static void beforeAll() {
    messageService = Sinch.conversationApi(Region.EU).messages();
  }

  @Test
  void testGetMessage() throws ApiException {
    final TypeConversationMessage response = messageService.get(messageId);
    prettyPrint(response);
  }

  @Test
  void testListMessages() throws ApiException {
    final V1ListMessagesResponse response =
        messageService.list(new ListMessagesParams().contactId(contactId));
    prettyPrint(response);
  }

  @Test
  void testSendAppMessage() throws ApiException {
    final V1SendMessageResponse response =
        messageService.send(
            new V1SendMessageRequest()
                .appId(appId)
                .message(
                    new TypeAppMessage()
                        .textMessage(new TypeTextMessage().text("SDK text message")))
                .recipient(new TypeRecipient().contactId(contactId))
                .addChannelPriorityOrderItem(TypeConversationChannel.SMS));
    prettyPrint(response);
  }

  @Test
  void testTranscodeMessage() throws ApiException {
    final Map<String, String> response =
        messageService.transcode(
            new V1TranscodeMessageRequest()
                .appMessage(
                    new TypeAppMessage()
                        .textMessage(new TypeTextMessage().text("SDK text message")))
                .addChannelsItem(TypeConversationChannel.VIBER)
                .addChannelsItem(TypeConversationChannel.WHATSAPP)
                .appId(appId));
    prettyPrint(response);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> messageService.get(messageId));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> messageService.list(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> messageService.list(new ListMessagesParams()));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> messageService.send(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> messageService.send(new V1SendMessageRequest()));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> messageService.transcode(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> messageService.transcode(new V1TranscodeMessageRequest().appId(appId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> messageService.transcode(new V1TranscodeMessageRequest().channels(List.of())));
    assertClientSideException(exception);
  }
}
