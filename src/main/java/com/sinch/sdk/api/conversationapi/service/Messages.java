package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.*;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Messages extends AbstractService {

  private static final String PARAM_MESSAGE_ID = "messageId";
  private static final String PARAM_SEND = "sendMessageRequest";
  private static final String PARAM_SEND_APP_ID = PARAM_SEND + SUB_APP_ID;
  private static final String PARAM_TRANSCODE = "transcodeMessageRequest";
  private static final String PARAM_TRANSCODE_APP_ID = PARAM_TRANSCODE + SUB_APP_ID;
  private static final String PARAM_TRANSCODE_CHANNELS = PARAM_TRANSCODE + SUB_CHANNELS;

  public Messages(
      final ConversationApiConfig config, final AuthenticationService authenticationService) {
    super(config, authenticationService);
  }

  @Override
  protected String getServiceName() {
    return "messages";
  }

  /**
   * Deletes a message that is part of a conversation. (blocking)
   *
   * <p>Removing the last message of a conversation will not delete the conversation.
   *
   * @param messageId The ID of the message to be deleted. (required)
   * @throws ApiException if fails to make API call
   */
  public void delete(final String messageId) {
    try {
      deleteAsync(messageId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Deletes a message that is part of a conversation.
   *
   * <p>Removing the last message of a conversation will not delete the conversation.
   *
   * @param messageId The ID of the message to be deleted. (required)
   * @return Async task of the delete call
   */
  public CompletableFuture<Void> deleteAsync(final String messageId) {
    if (StringUtils.isEmpty(messageId)) {
      return ExceptionUtils.missingParam(PARAM_MESSAGE_ID);
    }
    return restClient.delete(withPath(messageId));
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
  public ConversationMessage get(final String messageId) {
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
   * @return {@link ListMessagesResponse}
   * @throws ApiException if fails to make API call
   */
  public ListMessagesResponse list(final ListMessagesParams params) {
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
   * @return Async task generating {@link ListMessagesResponse}
   */
  public CompletableFuture<ListMessagesResponse> listAsync(final ListMessagesParams params) {
    if (params == null) {
      return ExceptionUtils.missingParam(PARAMS);
    }
    if (!params.isValid()) {
      return ExceptionUtils.missingOneOf(
          Conversations.PARAM_CONVERSATION_ID, Contacts.PARAM_CONTACT_ID);
    }
    return restClient.get(withQuery(params.build()), ListMessagesResponse.class);
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
   * @return {@link SendMessageResponse}
   * @throws ApiException if fails to make API call
   */
  public SendMessageResponse send(final SendMessageRequest sendMessageRequest) {
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
   * @return Async task generating a {@link SendMessageResponse}
   */
  public CompletableFuture<SendMessageResponse> sendAsync(
      final SendMessageRequest sendMessageRequest) {
    if (sendMessageRequest == null) {
      return ExceptionUtils.missingParam(PARAM_SEND);
    }
    if (StringUtils.isEmpty(sendMessageRequest.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_SEND_APP_ID);
    }
    return restClient.post(
        withQuery(":send"), SendMessageResponse.class, sendMessageRequest.projectId(projectId));
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
  public Map<String, String> transcode(final TranscodeMessageRequest transcodeMessageRequest) {
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
   * @return Async call generating a {@link TranscodeMessageResponse}
   */
  public CompletableFuture<Map<String, String>> transcodeAsync(
      final TranscodeMessageRequest transcodeMessageRequest) {
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
            TranscodeMessageResponse.class,
            transcodeMessageRequest.projectId(projectId))
        .thenApply(TranscodeMessageResponse::getTranscodedMessage);
  }
}
