package com.sinch.sdk.model.conversationapi.requests.messages;

import com.sinch.sdk.api.conversationapi.factories.MessageFactory;
import com.sinch.sdk.model.conversationapi.MediaMessage;

public class MediaMessageRequest extends AbstractMessageRequest<MediaMessageRequest, MediaMessage> {

  /**
   * Creates a Media message request with the supplied url.
   *
   * @param url Reference to the media
   */
  public MediaMessageRequest(final String url) {
    getAppMessage().setMediaMessage(MessageFactory.mediaMessage(url));
  }

  /**
   * Adds a thumbnail url to the message.
   *
   * @param url Reference to the thumbnail
   * @return this
   */
  public MediaMessageRequest thumbnail(final String url) {
    getMessage().setThumbnailUrl(url);
    return this;
  }

  @Override
  public MediaMessage getMessage() {
    return getAppMessage().getMediaMessage();
  }
}
