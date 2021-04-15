package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;

import com.sinch.sdk.api.conversationapi.model.ListConversationsParams;
import com.sinch.sdk.api.conversationapi.model.ListMessagesParams;
import com.sinch.sdk.model.conversationapi.Conversation;
import com.sinch.sdk.model.conversationapi.ConversationMessage;
import com.sinch.sdk.model.conversationapi.ListConversationsResponse;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ConversationsTest extends BaseServiceTest {

  private static final String CONTACT_ID = "contact-id";
  private static final String CONVERSATION_ID = "conversation-id";

  private static Conversations conversations;

  @BeforeEach
  void setUp() {
    conversations = new Conversations(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final Conversations conversations = new Conversations(CONFIG, null);
    assertThat(conversations.restClient).isNotNull();
    assertThat(conversations.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, conversations.getServiceName()));
  }

  @Test
  void createConversation() {
    conversations.create(new Conversation());

    verifyPostCalled(() -> eq(conversations.serviceURI), Conversation.class);
  }

  @Test
  void deleteConversation() {
    conversations.delete(CONVERSATION_ID);

    verifyDeleteCalled(() -> uriPathEndsWithMatcher(CONVERSATION_ID));
  }

  @Test
  void testGetConversation() {
    conversations.get(CONVERSATION_ID);

    verifyGetCalled(() -> uriPathEndsWithMatcher(CONVERSATION_ID), Conversation.class);
  }

  @Test
  void injectMessage() {
    conversations.injectMessage(
        new ConversationMessage().contactId(CONTACT_ID).conversationId(CONVERSATION_ID));

    verifyPostVoid(
        () -> uriPathEndsWithMatcher(CONVERSATION_ID + Conversations.INJECT_MESSAGE),
        () -> isA(ConversationMessage.class));
  }

  @Test
  void listConversations() {
    conversations.list(new ListConversationsParams().contactId(CONTACT_ID));

    verifyGetCalled(() -> uriQueryEndsWithMatcher(CONTACT_ID), ListConversationsResponse.class);
  }

  @Test
  void listMessages() {
    // For coverage, asserted in MessageTest
    conversations.listMessages(new ListMessagesParams().conversationId(CONVERSATION_ID));
    conversations.listMessagesAsync(new ListMessagesParams().conversationId(CONVERSATION_ID));
  }

  @Test
  void updateConversation() {
    conversations.update(CONVERSATION_ID, new Conversation().metadata("some meta data"));

    verifyPatchCalled(() -> uriPathEndsWithMatcher(CONVERSATION_ID), Conversation.class);
  }

  @Test
  void stopActiveConversation() {
    conversations.stopActive(CONVERSATION_ID);

    verifyPostVoid(() -> uriPathEndsWithMatcher(CONVERSATION_ID + Conversations.STOP));
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(final ThrowableAssert.ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowableAssert.ThrowingCallable> callsWithMissingParams() {
    return Arrays.asList(
        () -> conversations.create(null),
        () -> conversations.delete(null),
        () -> conversations.get(null),
        () -> conversations.injectMessage(null),
        () ->
            conversations.injectMessage(new ConversationMessage().conversationId(CONVERSATION_ID)),
        () -> conversations.injectMessage(new ConversationMessage().contactId(CONTACT_ID)),
        () -> conversations.list(null),
        () -> conversations.list(new ListConversationsParams()),
        () -> conversations.stopActive(null),
        () -> conversations.update(null, new Conversation()),
        () -> conversations.update(CONVERSATION_ID, null));
  }
}
