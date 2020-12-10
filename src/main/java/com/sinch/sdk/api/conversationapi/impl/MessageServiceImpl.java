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
import javax.validation.Valid;

public class MessageServiceImpl extends ConversationApiService implements MessageService {

  public MessageServiceImpl(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "messages";
  }

  @Override
  public SendAppMessageResponse sendAppMessage(
      @Valid final SendAppMessageRequest sendAppMessageRequest) {
    return restClient.post(withQuery(":send"), SendAppMessageResponse.class, sendAppMessageRequest);
  }

  @Override
  public ConversationMessage getMessage(final String messageId) {
    return restClient.get(withPath(messageId), ConversationMessage.class);
  }

  @Override
  public TranscodeMessageResponse transcodeMessage(
      @Valid final TranscodeMessageRequest transcodeMessageRequest) {
    return restClient.post(
        withQuery(":transcode"), TranscodeMessageResponse.class, transcodeMessageRequest);
  }

  @Override
  public ListMessagesResponse listMessages(final String contactId) {
    final String queryString =
        QueryStringBuilder.newInstance().add(CONTACT_PARAM, contactId).build();
    return restClient.get(withQuery(queryString), ListMessagesResponse.class);
  }
}
