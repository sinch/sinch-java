package com.sinch.sdk.api.conversationapi.service;

import static java.util.Collections.emptyList;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.model.ListMessagesParams;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.*;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import java.util.Map;
import java.util.Optional;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MessagesTest extends BaseConvIntegrationTest {

  private final String appId = "your-app-id";
  private final String contactId = "your-contact-id";
  private final String messageId = "your-message-id";

  private static Messages messages;

  @BeforeAll
  static void beforeAll() {
    messages =
        Sinch.conversationApi(Region.EU, () -> new OkHttpRestClientFactory(new OkHttpClient()))
            .messages();
  }

  @Test
  void testDeleteMessage() {
    messages.delete(messageId);
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> messages.get(messageId));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
    Assertions.assertEquals(
        Optional.of("404"), exception.getResponseHeaders().firstValue("status"));
    System.out.println(exception.getResponseBody());
  }

  @Test
  void testGetMessage() {
    final ConversationMessage response = messages.get(messageId);
    prettyPrint(response);
  }

  @Test
  void testListMessages() {
    final ListMessagesResponse response =
        messages.list(new ListMessagesParams().contactId(contactId));
    prettyPrint(response);
  }

  @Test
  void testSendAppMessage() {
    final SendMessageResponse response =
        messages.send(
            new SendMessageRequest()
                .appId(appId)
                .message(new AppMessage().textMessage(new TextMessage().text("SDK text message")))
                .recipient(new Recipient().contactId(contactId))
                .addChannelPriorityOrderItem(ConversationChannel.SMS));
    prettyPrint(response);
  }

  @Test
  void testTranscodeMessage() {
    final Map<String, String> response =
        messages.transcode(
            new TranscodeMessageRequest()
                .appMessage(
                    new AppMessage().textMessage(new TextMessage().text("SDK text message")))
                .addChannelsItem(ConversationChannel.VIBER)
                .addChannelsItem(ConversationChannel.WHATSAPP)
                .appId(appId));
    prettyPrint(response);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception = Assertions.assertThrows(ApiException.class, () -> messages.get(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> messages.list(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> messages.list(new ListMessagesParams()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> messages.send((SendMessageRequest) null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> messages.send(new SendMessageRequest()));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> messages.transcode(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> messages.transcode(new TranscodeMessageRequest().appId(appId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> messages.transcode(new TranscodeMessageRequest().channels(emptyList())));
    assertClientSideException(exception);
  }
}
