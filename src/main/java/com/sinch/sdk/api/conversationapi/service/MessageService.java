package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.*;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MessageService extends AbstractService {

  private static final String PARAM_MESSAGE_ID = "messageId";
  private static final String PARAM_SEND = "sendMessageRequest";
  private static final String PARAM_SEND_APP_ID = PARAM_SEND + SUB_APP_ID;
  private static final String PARAM_TRANSCODE = "transcodeMessageRequest";
  private static final String PARAM_TRANSCODE_APP_ID = PARAM_TRANSCODE + SUB_APP_ID;
  private static final String PARAM_TRANSCODE_CHANNELS = PARAM_TRANSCODE + SUB_CHANNELS;

  public MessageService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "messages";
  }

  /**
   * Get a message (blocking)
   *
   * <p>Retrieves a message by id.
   *
   * @param messageId The conversation message ID. (required)
   * @return {@link ConversationMessage}
   * @throws ApiException if fails to make API call
   */
  public ConversationMessage get(final String messageId) throws ApiException {
    try {
      return getAsync(messageId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Get a message
   *
   * <p>Retrieves a message by id.
   *
   * @param messageId The conversation message ID. (required)
   * @return Async task generating a {@link ConversationMessage}
   */
  public CompletableFuture<ConversationMessage> getAsync(final String messageId) {
    if (StringUtils.isEmpty(messageId)) {
      return ExceptionUtils.missingParam(PARAM_MESSAGE_ID);
    }
    return restClient.get(withPath(messageId), ConversationMessage.class);
  }

  /**
   * List messages (blocking)
   *
   * <p>This operation lists all messages associated with a conversation or a contact. The messages
   * are ordered by their accept_time property in descending order, where accept_time is a timestamp
   * of when the message was enqueued by the Conversation API. This means messages received most
   * recently will be listed first.
   *
   * @param params Object holding the parameters (required)
   * @return {@link V1ListMessagesResponse}
   * @throws ApiException if fails to make API call
   */
  public V1ListMessagesResponse list(final ListMessagesParams params) throws ApiException {
    try {
      return listAsync(params).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * List messages
   *
   * <p>This operation lists all messages associated with a conversation or a contact. The messages
   * are ordered by their accept_time property in descending order, where accept_time is a timestamp
   * of when the message was enqueued by the Conversation API. This means messages received most
   * recently will be listed first.
   *
   * @param params Object holding the parameters (required)
   * @return Async task generating {@link V1ListMessagesResponse}
   */
  public CompletableFuture<V1ListMessagesResponse> listAsync(final ListMessagesParams params) {
    if (params == null) {
      return ExceptionUtils.missingParam(PARAMS);
    }
    if (!params.isValid()) {
      return ExceptionUtils.missingOneOf(
          ConversationService.PARAM_CONVERSATION_ID, ContactService.PARAM_CONTACT_ID);
    }
    return restClient.get(withQuery(params.build()), V1ListMessagesResponse.class);
  }

  /**
   * Send a message (blocking)
   *
   * <p>Sends a message to the referenced contact from the referenced app. Note that this operation
   * enqueues the message in a queues so a successful response only indicates that the message has
   * been queued. The message is added to the active conversation with the contact if such
   * conversation exists. If no active conversation exists a new one is started automatically.
   *
   * @param sendMessageRequest (required)
   * @return {@link V1SendMessageResponse}
   * @throws ApiException if fails to make API call
   */
  public V1SendMessageResponse send(final V1SendMessageRequest sendMessageRequest)
      throws ApiException {
    try {
      return sendAsync(sendMessageRequest).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Send a message
   *
   * <p>Sends a message to the referenced contact from the referenced app. Note that this operation
   * enqueues the message in a queues so a successful response only indicates that the message has
   * been queued. The message is added to the active conversation with the contact if such
   * conversation exists. If no active conversation exists a new one is started automatically.
   *
   * @param sendMessageRequest (required)
   * @return Async task generating a {@link V1SendMessageResponse}
   */
  public CompletableFuture<V1SendMessageResponse> sendAsync(
      final V1SendMessageRequest sendMessageRequest) {
    if (sendMessageRequest == null) {
      return ExceptionUtils.missingParam(PARAM_SEND);
    }
    if (StringUtils.isEmpty(sendMessageRequest.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_SEND_APP_ID);
    }
    return restClient.post(
        withQuery(":send"), V1SendMessageResponse.class, sendMessageRequest.projectId(projectId));
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
  public Map<String, String> transcode(final V1TranscodeMessageRequest transcodeMessageRequest)
      throws ApiException {
    try {
      return transcodeAsync(transcodeMessageRequest).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
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
  public CompletableFuture<Map<String, String>> transcodeAsync(
      final V1TranscodeMessageRequest transcodeMessageRequest) {
    if (transcodeMessageRequest == null) {
      return ExceptionUtils.missingParam(PARAM_TRANSCODE);
    }
    if (StringUtils.isEmpty(transcodeMessageRequest.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_TRANSCODE_APP_ID);
    }
    if (transcodeMessageRequest.getChannels() == null) {
      return ExceptionUtils.missingParam(PARAM_TRANSCODE_CHANNELS);
    }
    return restClient
        .post(
            withQuery(":transcode"),
            V1TranscodeMessageResponse.class,
            transcodeMessageRequest.projectId(projectId))
        .thenApply(V1TranscodeMessageResponse::getTranscodedMessage);
  }
}
