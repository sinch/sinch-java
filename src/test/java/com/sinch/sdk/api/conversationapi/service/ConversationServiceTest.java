package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
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
  void testCreateConversation() throws ApiException {
    final TypeConversation response =
        conversationService.create(
            new TypeConversation()
                .active(true)
                .activeChannel(TypeConversationChannel.MESSENGER)
                .appId(appId)
                .contactId(contactId));
    prettyPrint(response);
  }

  @Test
  void testGetConversation() throws ApiException {
    final TypeConversation conversation = conversationService.get(conversationId);
    prettyPrint(conversation);
  }

  @Test
  void testInjectMessage() throws ApiException {
    conversationService.injectMessage(
        new TypeConversationMessage()
            .contactId(contactId)
            .conversationId(conversationId)
            .direction(TypeConversationDirection.TO_APP)
            .channelIdentity(new TypeChannelIdentity().identity("channel identity"))
            .contactMessage(
                new TypeContactMessage().textMessage(new TypeTextMessage().text("Hi"))));
  }

  @Test
  void testListConversations() throws ApiException {
    final V1ListConversationsResponse response =
        conversationService.listConversations(
            new ListConversationsParams().onlyActive(true).appId(appId).size(1));
    prettyPrint(response);
  }

  @Test
  void testListMessages() throws ApiException {
    final V1ListMessagesResponse response =
        conversationService.listMessages(new ListMessagesParams().conversationId(conversationId));
    prettyPrint(response);
  }

  @Test
  void testUpdateConversation() throws ApiException {
    final TypeConversation conversation =
        conversationService.update(
            conversationId, new TypeConversation().metadata("some meta data"));
    prettyPrint(conversation);
  }

  @Test
  void testStopActiveConversation() throws ApiException {
    conversationService.stopActive(conversationId);
    final TypeConversation conversation = conversationService.get(conversationId);
    prettyPrint(conversation);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> conversationService.create(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> conversationService.get(conversationId));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> conversationService.injectMessage(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () ->
                conversationService.injectMessage(
                    new TypeConversationMessage().conversationId(conversationId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () ->
                conversationService.injectMessage(
                    new TypeConversationMessage().contactId(contactId)));
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
            ApiException.class, () -> conversationService.update(null, new TypeConversation()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> conversationService.update(conversationId, null));
    assertClientSideException(exception);
  }
}
