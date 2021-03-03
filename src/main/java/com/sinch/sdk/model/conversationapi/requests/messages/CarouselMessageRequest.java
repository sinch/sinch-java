package com.sinch.sdk.model.conversationapi.requests.messages;

import com.sinch.sdk.api.conversationapi.factories.MessageFactory;
import com.sinch.sdk.model.conversationapi.CardMessage;
import com.sinch.sdk.model.conversationapi.CarouselMessage;
import com.sinch.sdk.model.conversationapi.Choice;

public class CarouselMessageRequest
    extends AbstractMessageRequest<CarouselMessageRequest, CarouselMessage>
    implements ChoiceSupport<CarouselMessageRequest> {

  /**
   * Creates a Carousel message request containing a list of cards often rendered horizontally on
   * supported channels. Supported types for media are only images, e.g. .png, .jpg, .jpeg
   * extensions.
   */
  public CarouselMessageRequest() {
    getAppMessage().setCarouselMessage(MessageFactory.carouselMessage());
  }

  /**
   * Adds a card to the message (required, max 10 cards per message)
   *
   * @param card The card message to add
   * @return this
   */
  public CarouselMessageRequest addCard(final CardMessage card) {
    getMessage().addCardsItem(card);
    return this;
  }

  @Override
  public CarouselMessageRequest addChoice(final Choice choice) {
    getMessage().addChoicesItem(choice);
    return this;
  }

  @Override
  public CarouselMessage getMessage() {
    return getAppMessage().getCarouselMessage();
  }
}
