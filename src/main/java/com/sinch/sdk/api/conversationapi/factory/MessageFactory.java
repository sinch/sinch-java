package com.sinch.sdk.api.conversationapi.factory;

import com.sinch.sdk.model.conversationapi.*;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageFactory {

  public static CardMessage cardMessage(final String title) {
    return new CardMessage().title(title);
  }

  public static CarouselMessage carouselMessage(final CardMessage... cards) {
    final CarouselMessage carouselMessage = new CarouselMessage();
    Stream.of(cards).forEach(carouselMessage::addCardsItem);
    return carouselMessage;
  }

  public static ChoiceMessage choiceMessage(final String message) {
    return new ChoiceMessage().textMessage(textMessage(message));
  }

  public static LocationMessage locationMessage(
      final String title, final float lon, final float lat) {
    return new LocationMessage()
        .title(title)
        .coordinates(new Coordinates().longitude(lon).latitude(lat));
  }

  public static MediaMessage mediaMessage(final String url) {
    return new MediaMessage().url(url);
  }

  public static TemplateMessage templateMessage() {
    return new TemplateMessage();
  }

  public static TextMessage textMessage(final String message) {
    return new TextMessage().text(message);
  }

  public static CallMessage callMessage(final String title, final String number) {
    return new CallMessage().title(title).phoneNumber(number);
  }

  public static UrlMessage urlMessage(final String title, final String url) {
    return new UrlMessage().title(title).url(url);
  }
}
