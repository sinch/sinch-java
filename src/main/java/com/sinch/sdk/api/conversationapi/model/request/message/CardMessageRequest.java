package com.sinch.sdk.api.conversationapi.model.request.message;

import com.sinch.sdk.api.conversationapi.factory.MessageFactory;
import com.sinch.sdk.model.conversationapi.CardHeight;
import com.sinch.sdk.model.conversationapi.CardMessage;
import com.sinch.sdk.model.conversationapi.Choice;

public class CardMessageRequest extends AbstractMessageRequest<CardMessageRequest, CardMessage>
    implements ChoiceSupport<CardMessageRequest> {

  /**
   * Creates a Card message request with the supplied title.
   *
   * @param title The message title
   */
  public CardMessageRequest(final String title) {
    getAppMessage().setCardMessage(MessageFactory.cardMessage(title));
  }

  /**
   * Adds a card description.
   *
   * @param description The description text
   * @return this
   */
  public CardMessageRequest description(final String description) {
    getMessage().setDescription(description);
    return this;
  }

  /**
   * Adds a media to the message.
   *
   * @param url The resource link to the media
   * @return this
   */
  public CardMessageRequest media(final String url) {
    getMessage().setMediaMessage(MessageFactory.mediaMessage(url));
    return this;
  }

  /**
   * Sets the card height, will be translated to MEDIUM in the api if omitted.
   *
   * @param height the card height
   * @return this
   */
  public CardMessageRequest height(final CardHeight height) {
    getMessage().setHeight(height);
    return this;
  }

  @Override
  public CardMessageRequest addChoice(final Choice choice) {
    getMessage().addChoicesItem(choice);
    return this;
  }

  @Override
  public CardMessage getMessage() {
    return getAppMessage().getCardMessage();
  }
}
