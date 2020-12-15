package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.ConversationService;
import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import com.sinch.sdk.model.conversationapi.conversation.service.ListConversationsResponse;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.utils.QueryStringBuilder;
import javax.validation.Valid;
import lombok.NonNull;

public class ConversationServiceImpl extends ConversationApiService implements ConversationService {

  public ConversationServiceImpl(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "conversations";
  }

  @Override
  public Conversation createConversation(@Valid final Conversation conversation) {
    return restClient.post(serviceURI, Conversation.class, conversation);
  }

  @Override
  public Conversation getConversation(final String conversationId) {
    return restClient.get(withPath(conversationId), Conversation.class);
  }

  @Override
  public Conversation updateConversation(
      @Valid final Conversation conversation, final String conversationId) {
    return restClient.patch(withPath(conversationId), Conversation.class, conversation);
  }

  @Override
  public ListConversationsResponse listConversationsByApp(
      final String appId,
      final boolean activeOnly,
      final Integer pageSize,
      final String pageToken) {
    final String queryString =
        getQueryBuilder(activeOnly, pageSize, pageToken).add("app_id", appId).build();
    return restClient.get(withQuery(queryString), ListConversationsResponse.class);
  }

  @Override
  public ListConversationsResponse listConversationsByContact(
      final String contactId,
      final boolean activeOnly,
      final Integer pageSize,
      final String pageToken) {
    final String queryString =
        getQueryBuilder(activeOnly, pageSize, pageToken).add(CONTACT_PARAM, contactId).build();
    return restClient.get(withQuery(queryString), ListConversationsResponse.class);
  }

  @Override
  public void stopActiveConversation(@NonNull final String conversationId) {
    restClient.post(withPath(conversationId.concat(":stop")));
  }

  @Override
  public void injectMessageIntoConversation(
      @Valid final ConversationMessage conversationMessage, @NonNull final String conversationId) {
    restClient.patch(withPath(conversationId.concat(":inject-message")), conversationMessage);
  }

  private QueryStringBuilder getQueryBuilder(
      final boolean activeOnly, final Integer pageSize, final String pageToken) {
    return QueryStringBuilder.newInstance()
        .add("active_only", activeOnly)
        .add(PAGE_SIZE_PARAM, pageSize)
        .add(PAGE_TOKEN_PARAM, pageToken);
  }
}
