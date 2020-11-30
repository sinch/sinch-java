package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.conversation.service.ListMessagesResponse;
import com.sinch.sdk.model.conversationapi.message.ConversationMessage;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageRequest;
import com.sinch.sdk.model.conversationapi.message.service.SendAppMessageResponse;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.transcoding.service.TranscodeMessageResponse;
import javax.validation.Valid;

public interface MessageService {

  SendAppMessageResponse sendAppMessage(@Valid SendAppMessageRequest sendAppMessageRequest);

  ConversationMessage getMessage(String messageId);

  TranscodeMessageResponse transcodeMessage(@Valid TranscodeMessageRequest transcodeMessageRequest);

  // TODO: query params??
  ListMessagesResponse listMessages(String contactId);
}
