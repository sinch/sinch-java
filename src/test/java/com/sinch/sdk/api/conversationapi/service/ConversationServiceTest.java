package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ConversationServiceTest extends BaseConvIntegrationTest {

  private final String appId = "your-app-id";
  private final String contactId = "your-contact-id";
  private final String conversationId = "your-conversation-id";

  private static ConversationService conversationService;

  @BeforeAll
  static void beforeAll() {
    conversationService = Sinch.conversationApi(Region.EU).conversations();
  }

  @Test
  void testCreateConversation() {
    final Conversation response =
        conversationService.create(
            new Conversation()
                .active(true)
                .activeChannel(ConversationChannel.MESSENGER)
                .appId(appId)
                .contactId(contactId));
    prettyPrint(response);
  }

  @Test
  void testDeleteConversation() {
    conversationService.delete(conversationId);
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> conversationService.get(conversationId));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
    Assertions.assertEquals(
        Optional.of("404"), exception.getResponseHeaders().firstValue(":status"));
    System.out.println(exception.getResponseBody());
  }

  @Test
  void testGetConversation() {
    final Conversation conversation = conversationService.get(conversationId);
    prettyPrint(conversation);
  }

  @Test
  void testInjectMessage() {
    conversationService.injectMessage(
        new ConversationMessage()
            .contactId(contactId)
            .conversationId(conversationId)
            .direction(ConversationDirection.TO_APP)
            .channelIdentity(new ChannelIdentity().identity("channel identity"))
            .contactMessage(new ContactMessage().textMessage(new TextMessage().text("Hi"))));
  }

  @Test
  void testListConversations() {
    final ListConversationsResponse response =
        conversationService.listConversations(
            new ListConversationsParams().onlyActive(true).appId(appId).size(1));
    prettyPrint(response);
  }

  @Test
  void testListMessages() {
    final ListMessagesResponse response =
        conversationService.listMessages(new ListMessagesParams().conversationId(conversationId));
    prettyPrint(response);
  }

  @Test
  void testUpdateConversation() {
    final Conversation conversation =
        conversationService.update(conversationId, new Conversation().metadata("some meta data"));
    prettyPrint(conversation);
  }

  @Test
  void testStopActiveConversation() {
    conversationService.stopActive(conversationId);
    final Conversation conversation = conversationService.get(conversationId);
    prettyPrint(conversation);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> conversationService.create(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> conversationService.get(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> conversationService.injectMessage(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () ->
                conversationService.injectMessage(
                    new ConversationMessage().conversationId(conversationId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () ->
                conversationService.injectMessage(new ConversationMessage().contactId(contactId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> conversationService.listConversations(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> conversationService.listConversations(new ListConversationsParams()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> conversationService.stopActive(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> conversationService.update(null, new Conversation()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> conversationService.update(conversationId, null));
    assertClientSideException(exception);
  }
}
