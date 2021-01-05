package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.conversation.service.ListMessagesResponse;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageRequest;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageResponse;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageResponse;
import com.sinch.sdk.utils.QueryStringBuilder;
import javax.validation.Valid;

public class MessageService extends ConversationApiService {

  public MessageService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "messages";
  }

  public SendAppMessageResponse sendAppMessage(
      @Valid final SendAppMessageRequest sendAppMessageRequest) {
    return restClient.post(withQuery(":send"), SendAppMessageResponse.class, sendAppMessageRequest);
  }

  public ConversationMessage getMessage(final String messageId) {
    return restClient.get(withPath(messageId), ConversationMessage.class);
  }

  public TranscodeMessageResponse transcodeMessage(
      @Valid final TranscodeMessageRequest transcodeMessageRequest) {
    return restClient.post(
        withQuery(":transcode"), TranscodeMessageResponse.class, transcodeMessageRequest);
  }

  public ListMessagesResponse listMessages(final String contactId) {
    final String queryString =
        QueryStringBuilder.newInstance().add(CONTACT_PARAM, contactId).build();
    return restClient.get(withQuery(queryString), ListMessagesResponse.class);
  }
}
