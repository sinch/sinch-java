package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import com.sinch.sdk.model.conversationapi.conversation.service.ListConversationsResponse;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import javax.validation.Valid;

public interface ConversationService {

  Conversation createConversation(@Valid Conversation conversation);

  Conversation getConversation(@Valid String conversationId);

  Conversation updateConversation(@Valid Conversation conversation, String conversationId);

  // TODO: allow for less args!
  ListConversationsResponse listConversationsByApp(
      String appId, boolean activeOnly, Integer pageSize, String pageToken);

  // TODO: allow for less args!
  ListConversationsResponse listConversationsByContact(
      String contactId, boolean activeOnly, Integer pageSize, String pageToken);

  void stopActiveConversation(String conversationId);

  void injectMessageIntoConversation(
      @Valid ConversationMessage conversationMessage, String conversationId);
}
