package com.sinch.sdk.api.conversationapi.model.request.message;

import com.sinch.sdk.api.conversationapi.factory.MessageFactory;
import com.sinch.sdk.model.conversationapi.Choice;
import com.sinch.sdk.model.conversationapi.ChoiceMessage;

public class ChoiceMessageRequest
    extends AbstractMessageRequest<ChoiceMessageRequest, ChoiceMessage>
    implements ChoiceSupport<ChoiceMessageRequest> {

  /**
   * Creates a Choice message request with the supplied message.
   *
   * @param message The message text
   */
  public ChoiceMessageRequest(final String message) {
    getAppMessage().setChoiceMessage(MessageFactory.choiceMessage(message));
  }

  @Override
  public ChoiceMessageRequest addChoice(final Choice choice) {
    getMessage().addChoicesItem(choice);
    return this;
  }

  @Override
  public ChoiceMessage getMessage() {
    return getAppMessage().getChoiceMessage();
  }
}
