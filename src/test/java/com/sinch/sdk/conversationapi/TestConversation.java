package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import com.sinch.sdk.model.conversationapi.message.ContactMessage;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.model.conversationapi.message.type.MediaMessage;
import org.junit.jupiter.api.Test;

public class TestConversation extends AbstractTest{
  private static final String contactId = "your-contact-id";
  private static final String conversationId = "your-conversation-id";
  private static final String appId = "your-app-id";

  @Test
  public void testCreateConversation() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Conversation con =
        Conversation.builder()
            .active(true)
            .activeChannel(ConversationChannel.MESSENGER)
            .appId(appId)
            .contactId(contactId)
            .build();

    Conversation response = client.getConversationService().createConversation(con);
    System.out.println(response);
  }

  @Test
  public void testGetConversation() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Conversation response =
        client.getConversationService().getConversation(conversationId);
    System.out.println(response);
  }

  @Test
  public void testUpdateConversation() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Conversation con =
        Conversation.builder()
            .active(true)
            .activeChannel(ConversationChannel.MESSENGER)
            .appId(appId)
            .contactId(contactId)
            .metadata("some metadata")
            .build();

    Conversation response =
        client.getConversationService().updateConversation(con, conversationId);
    System.out.println(response);
  }

  @Test
  public void testStopActiveConversation() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    client.getConversationService().stopActiveConversation(conversationId);

    Conversation response =
        client.getConversationService().getConversation(conversationId);
    System.out.println(response);
  }

  @Test
  public void testInjectMsgToConversation() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    ConversationMessage message =
        ConversationMessage.fromContactMessage()
            .contactMessage(
                ContactMessage.fromMediaMessage()
                    .mediaMessage(MediaMessage.builder().url("68769").build())
                    .build())
            .build();

    client
        .getConversationService()
        .injectMessageIntoConversation(message, conversationId);
  }
}
