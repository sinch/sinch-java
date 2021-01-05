package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import com.sinch.sdk.model.conversationapi.conversation.service.ListConversationsResponse;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.utils.QueryStringBuilder;
import javax.validation.Valid;
import lombok.NonNull;

public class ConversationService extends AbstractService {

  public ConversationService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "conversations";
  }

  public Conversation createConversation(@Valid final Conversation conversation) {
    return restClient.post(serviceURI, Conversation.class, conversation);
  }

  public Conversation getConversation(final String conversationId) {
    return restClient.get(withPath(conversationId), Conversation.class);
  }

  public Conversation updateConversation(
      @Valid final Conversation conversation, final String conversationId) {
    return restClient.patch(withPath(conversationId), Conversation.class, conversation);
  }

  public ListConversationsResponse listConversationsByApp(
      final String appId,
      final boolean activeOnly,
      final Integer pageSize,
      final String pageToken) {
    final String queryString =
        getQueryBuilder(activeOnly, pageSize, pageToken).add("app_id", appId).build();
    return restClient.get(withQuery(queryString), ListConversationsResponse.class);
  }

  public ListConversationsResponse listConversationsByContact(
      final String contactId,
      final boolean activeOnly,
      final Integer pageSize,
      final String pageToken) {
    final String queryString =
        getQueryBuilder(activeOnly, pageSize, pageToken).add(CONTACT_PARAM, contactId).build();
    return restClient.get(withQuery(queryString), ListConversationsResponse.class);
  }

  public void stopActiveConversation(@NonNull final String conversationId) {
    restClient.post(withPath(conversationId.concat(":stop")));
  }

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
