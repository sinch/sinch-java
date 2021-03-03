package com.sinch.sdk.model.conversationapi.requests.messages;

import com.sinch.sdk.api.conversationapi.factories.MessageFactory;
import com.sinch.sdk.model.conversationapi.TextMessage;

public class TextMessageRequest extends AbstractMessageRequest<TextMessageRequest, TextMessage> {

  /**
   * Creates a Text message request with the supplied message.
   *
   * @param message The message
   */
  public TextMessageRequest(final String message) {
    getAppMessage().setTextMessage(MessageFactory.textMessage(message));
  }

  @Override
  public TextMessage getMessage() {
    return getAppMessage().getTextMessage();
  }
}
