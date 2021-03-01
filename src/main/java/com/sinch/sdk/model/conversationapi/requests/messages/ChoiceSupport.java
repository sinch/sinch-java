package com.sinch.sdk.model.conversationapi.requests.messages;

import static com.sinch.sdk.api.conversationapi.factories.ChoiceFactory.choice;

import com.sinch.sdk.api.conversationapi.factories.MessageFactory;
import com.sinch.sdk.model.conversationapi.Choice;

public interface ChoiceSupport<T> {

  /**
   * A choice is an action the user can take such as buttons for quick replies, call actions etc.
   *
   * <p>Use this function to add choices with optional fields (max 3)
   *
   * @param choice the choice
   * @return this
   */
  T addChoice(final Choice choice);

  /**
   * Adds a Call action choice to the message. (max 3 choices in total)
   *
   * @param title The choice title
   * @param number The associated number
   * @return this
   */
  default T addCallChoice(final String title, final String number) {
    return addChoice(choice(MessageFactory.callMessage(title, number)));
  }

  /**
   * Adds a Location choice to the message. (max 3 choices in total)
   *
   * @param title The choice title
   * @param lon The location longitude
   * @param lat The location latitude
   * @return this
   */
  default T addLocationChoice(final String title, final float lon, final float lat) {
    return addChoice(choice(MessageFactory.locationMessage(title, lon, lat)));
  }

  /**
   * Adds a Text choice to the message. (max 3 choices in total)
   *
   * @param message The choice message
   * @return this
   */
  default T addTextChoice(final String message) {
    return addChoice(choice(MessageFactory.textMessage(message)));
  }

  /**
   * Adds a Url choice to the message. (max 3 choices in total)
   *
   * @param title The choice title
   * @param url The choice url
   * @return this
   */
  default T addUrlChoice(final String title, final String url) {
    return addChoice(choice(MessageFactory.urlMessage(title, url)));
  }
}
