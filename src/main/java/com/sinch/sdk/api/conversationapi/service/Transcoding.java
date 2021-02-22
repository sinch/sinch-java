package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.TranscodeMessageResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Transcoding extends AbstractService {

  private final Messages messages;

  public Transcoding(
      final ConversationApiConfig config, final AuthenticationService authenticationService) {
    super(config, authenticationService);
    messages = new Messages(config, authenticationService);
  }

  @Override
  protected String getServiceName() {
    return "";
  }

  /**
   * Transcode a message (blocking)
   *
   * <p>Transcodes the message from the Conversation API format to the channel-specific formats for
   * the requested channels. No message is sent to the contact.
   *
   * @param transcodeMessageRequest (required)
   * @return {@link TranscodeMessageResponse}
   * @throws ApiException if fails to make API call
   */
  public Map<String, String> transcodeMessage(
      final TranscodeMessageRequest transcodeMessageRequest) {
    return messages.transcode(transcodeMessageRequest);
  }

  /**
   * Transcode a message
   *
   * <p>Transcodes the message from the Conversation API format to the channel-specific formats for
   * the requested channels. No message is sent to the contact.
   *
   * @param transcodeMessageRequest (required)
   * @return Async call generating a {@link TranscodeMessageResponse}
   */
  public CompletableFuture<Map<String, String>> transcodeMessageAsync(
      final TranscodeMessageRequest transcodeMessageRequest) {
    return messages.transcodeAsync(transcodeMessageRequest);
  }
}
