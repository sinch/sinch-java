package com.sinch.sdk.api.conversationapi.factory;

import com.sinch.sdk.model.conversationapi.CallMessage;
import com.sinch.sdk.model.conversationapi.Choice;
import com.sinch.sdk.model.conversationapi.LocationMessage;
import com.sinch.sdk.model.conversationapi.TextMessage;
import com.sinch.sdk.model.conversationapi.UrlMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChoiceFactory {

  public static Choice choice(final CallMessage message) {
    return new Choice().callMessage(message);
  }

  public static Choice choice(final LocationMessage message) {
    return new Choice().locationMessage(message);
  }

  public static Choice choice(final TextMessage message) {
    return new Choice().textMessage(message);
  }

  public static Choice choice(final UrlMessage message) {
    return new Choice().urlMessage(message);
  }
}
