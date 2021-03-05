package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.model.ListConversationsParams;
import com.sinch.sdk.api.conversationapi.model.ListMessagesParams;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.*;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import java.util.Optional;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ConversationsTest extends BaseConvIntegrationTest {

  private final String appId = "your-app-id";
  private final String contactId = "your-contact-id";
  private final String conversationId = "your-conversation-id";

  private static Conversations conversations;

  @BeforeAll
  static void beforeAll() {
    conversations =
        Sinch.conversationApi(Region.EU, () -> new OkHttpRestClientFactory(new OkHttpClient()))
            .conversations();
  }

  @Test
  void testCreateConversation() {
    final Conversation response =
        conversations.create(
            new Conversation()
                .active(true)
                .activeChannel(ConversationChannel.MESSENGER)
                .appId(appId)
                .contactId(contactId));
    prettyPrint(response);
  }

  @Test
  void testDeleteConversation() {
    conversations.delete(conversationId);
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> conversations.get(conversationId));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
    Assertions.assertEquals(
        Optional.of("404"), exception.getResponseHeaders().firstValue("status"));
    System.out.println(exception.getResponseBody());
  }

  @Test
  void testGetConversation() {
    final Conversation conversation = conversations.get(conversationId);
    prettyPrint(conversation);
  }

  @Test
  void testInjectMessage() {
    conversations.injectMessage(
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
        conversations.listConversations(
            new ListConversationsParams().onlyActive(true).appId(appId).size(1));
    prettyPrint(response);
  }

  @Test
  void testListMessages() {
    final ListMessagesResponse response =
        conversations.listMessages(new ListMessagesParams().conversationId(conversationId));
    prettyPrint(response);
  }

  @Test
  void testUpdateConversation() {
    final Conversation conversation =
        conversations.update(conversationId, new Conversation().metadata("some meta data"));
    prettyPrint(conversation);
  }

  @Test
  void testStopActiveConversation() {
    conversations.stopActive(conversationId);
    final Conversation conversation = conversations.get(conversationId);
    prettyPrint(conversation);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> conversations.create(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> conversations.get(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> conversations.injectMessage(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () ->
                conversations.injectMessage(
                    new ConversationMessage().conversationId(conversationId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> conversations.injectMessage(new ConversationMessage().contactId(contactId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> conversations.listConversations(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> conversations.listConversations(new ListConversationsParams()));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> conversations.stopActive(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> conversations.update(null, new Conversation()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> conversations.update(conversationId, null));
    assertClientSideException(exception);
  }
}
