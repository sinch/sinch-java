package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.MessageService;
import com.sinch.sdk.model.conversationapi.conversation.service.ListMessagesResponse;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageRequest;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageResponse;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageResponse;
import com.sinch.sdk.utils.QueryStringBuilder;
import java.util.function.Supplier;
import javax.validation.Valid;

public class MessageServiceImpl extends ConversationApiService implements MessageService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s/messages";
  private static final String CONTACT_PARAM = "contact_id";

  public MessageServiceImpl(ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public SendAppMessageResponse sendAppMessage(@Valid SendAppMessageRequest sendAppMessageRequest) {
    return postRequest(":send", SendAppMessageResponse.class, sendAppMessageRequest);
  }

  @Override
  public ConversationMessage getMessage(String messageId) {
    return getRequest("/".concat(messageId), ConversationMessage.class);
  }

  @Override
  public TranscodeMessageResponse transcodeMessage(
      @Valid TranscodeMessageRequest transcodeMessageRequest) {
    return postRequest(":transcode", TranscodeMessageResponse.class, transcodeMessageRequest);
  }

  @Override
  public ListMessagesResponse listMessages(String contactId) {
    final String queryString =
        QueryStringBuilder.newInstance().add(CONTACT_PARAM, contactId).build();
    return getRequest(queryString, ListMessagesResponse.class);
  }
}
