package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.ConversationService;
import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import com.sinch.sdk.model.conversationapi.conversation.service.ListConversationsResponse;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.utils.QueryStringBuilder;
import java.util.function.Supplier;
import javax.validation.Valid;

public class ConversationServiceImpl extends ConversationApiService implements ConversationService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s/conversations";
  private static final String CONTACT_PARAM = "contact_id";
  private static final String APP_PARAM = "app_id";
  private static final String PAGE_SIZE_PARAM = "page_size";
  private static final String PAGE_TOKEN_PARAM = "page_token";
  private static final String ACTIVE_PARAM = "active_only";

  public ConversationServiceImpl(
      ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public Conversation createConversation(@Valid Conversation conversation) {
    return postRequest("", Conversation.class, conversation);
  }

  @Override
  public Conversation getConversation(String conversationId) {
    return getRequest("/".concat(conversationId), Conversation.class);
  }

  @Override
  public Conversation updateConversation(@Valid Conversation conversation, String conversationId) {
    return patchRequest("/".concat(conversationId), Conversation.class, conversation);
  }

  @Override
  public ListConversationsResponse listConversationsByApp(
      String appId, boolean activeOnly, Integer pageSize, String pageToken) {
    final String queryString =
        getQueryBuilder(activeOnly, pageSize, pageToken).add(APP_PARAM, appId).build();
    return getRequest(queryString, ListConversationsResponse.class);
  }

  @Override
  public ListConversationsResponse listConversationsByContact(
      String contactId, boolean activeOnly, Integer pageSize, String pageToken) {
    final String queryString =
        getQueryBuilder(activeOnly, pageSize, pageToken).add(CONTACT_PARAM, contactId).build();
    return getRequest(queryString, ListConversationsResponse.class);
  }

  @Override
  public void stopActiveConversation(String conversationId) {
    postRequestEmptyBody("/".concat(conversationId).concat(":stop"));
  }

  @Override
  public void injectMessageIntoConversation(
      @Valid ConversationMessage conversationMessage, String conversationId) {
    postRequestEmptyBody("/".concat(conversationId).concat(":inject-message"));
  }

  private QueryStringBuilder getQueryBuilder(
      final boolean activeOnly, final Integer pageSize, final String pageToken) {
    return QueryStringBuilder.newInstance()
        .add(ACTIVE_PARAM, activeOnly)
        .add(PAGE_SIZE_PARAM, pageSize)
        .add(PAGE_TOKEN_PARAM, pageToken);
  }
}
