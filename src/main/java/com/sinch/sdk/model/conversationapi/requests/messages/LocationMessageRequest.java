package com.sinch.sdk.model.conversationapi.requests.messages;

import com.sinch.sdk.api.conversationapi.factories.MessageFactory;
import com.sinch.sdk.model.conversationapi.LocationMessage;

public class LocationMessageRequest
    extends AbstractMessageRequest<LocationMessageRequest, LocationMessage> {

  /**
   * Creates a Location message request with the supplied title and coordinates.
   *
   * @param title The location title
   * @param lon The location longitude
   * @param lat The location latitude
   */
  public LocationMessageRequest(final String title, final float lon, final float lat) {
    getAppMessage().setLocationMessage(MessageFactory.locationMessage(title, lon, lat));
  }

  /**
   * Adds a label to the location message.
   *
   * @param label The label text
   * @return this
   */
  public LocationMessageRequest label(final String label) {
    getMessage().setLabel(label);
    return this;
  }

  @Override
  public LocationMessage getMessage() {
    return getAppMessage().getLocationMessage();
  }
}
