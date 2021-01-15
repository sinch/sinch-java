package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.V1TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.V1TranscodeMessageResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TranscodingService extends AbstractService {

  private final MessageService messageService;

  public TranscodingService(final ConversationApiConfig config) {
    super(config);
    messageService = new MessageService(config);
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
   * @return {@link V1TranscodeMessageResponse}
   * @throws ApiException if fails to make API call
   */
  public Map<String, String> transcodeMessage(
      final V1TranscodeMessageRequest transcodeMessageRequest) throws ApiException {
    return messageService.transcode(transcodeMessageRequest);
  }

  /**
   * Transcode a message
   *
   * <p>Transcodes the message from the Conversation API format to the channel-specific formats for
   * the requested channels. No message is sent to the contact.
   *
   * @param transcodeMessageRequest (required)
   * @return Async call generating a {@link V1TranscodeMessageResponse}
   */
  public CompletableFuture<Map<String, String>> transcodeMessageAsync(
      final V1TranscodeMessageRequest transcodeMessageRequest) {
    return messageService.transcodeAsync(transcodeMessageRequest);
  }
}
