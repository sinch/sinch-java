package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import com.sinch.sdk.model.conversationapi.message.ContactMessage;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.model.conversationapi.message.type.MediaMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ConversationServiceTest extends BaseTest {

  private final String appId = "your-app-id";
  private final String contactId = "your-contact-id";
  private final String conversationId = "your-conversation-id";

  private static ConversationService conversationService;

  @BeforeAll
  static void beforeAll() {
    conversationService = Sinch.conversationApi().getConversationService();
  }

  @Test
  void testCreateConversation() {
    final Conversation con =
        Conversation.builder()
            .active(true)
            .activeChannel(ConversationChannel.MESSENGER)
            .appId(appId)
            .contactId(contactId)
            .build();

    final Conversation response = conversationService.createConversation(con);
    prettyPrint(response);
  }

  @Test
  void testGetConversation() {
    final Conversation response = conversationService.getConversation(conversationId);
    prettyPrint(response);
  }

  @Test
  void testUpdateConversation() {
    final Conversation con =
        Conversation.builder()
            .active(true)
            .activeChannel(ConversationChannel.MESSENGER)
            .appId(appId)
            .contactId(contactId)
            .metadata("some metadata")
            .build();

    final Conversation response = conversationService.updateConversation(con, conversationId);
    prettyPrint(response);
  }

  @Test
  void testStopActiveConversation() {
    conversationService.stopActiveConversation(conversationId);
    final Conversation response = conversationService.getConversation(conversationId);
    prettyPrint(response);
  }

  @Test
  void testInjectMessageIntoConversation() {
    final ConversationMessage message =
        ConversationMessage.fromContactMessage()
            .contactMessage(
                ContactMessage.fromMediaMessage()
                    .mediaMessage(MediaMessage.builder().url("68769").build())
                    .build())
            .build();

    conversationService.injectMessageIntoConversation(message, conversationId);
  }
}
