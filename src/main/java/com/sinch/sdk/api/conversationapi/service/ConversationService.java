package com.sinch.sdk.api.conversationapi.service;

import static com.sinch.sdk.api.conversationapi.service.ContactService.PARAM_CONTACT_ID;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.*;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ConversationService extends AbstractService {

  static final String PARAM_CONVERSATION_ID = "conversationId";
  private static final String PARAM_CONVERSATION = "conversation";
  private static final String PARAM_CONVERSATION_MESSAGE = "conversationMessage";
  private static final String PARAM_MESSAGE_CONVERSATION_ID =
      PARAM_CONVERSATION_MESSAGE + '.' + PARAM_CONVERSATION_ID;
  private static final String PARAM_MESSAGE_CONTACT_ID =
      PARAM_CONVERSATION_MESSAGE + "." + PARAM_CONTACT_ID;

  private final MessageService messageService;

  public ConversationService(final ConversationApiConfig config) {
    super(config);
    messageService = new MessageService(config);
  }

  @Override
  protected String getServiceName() {
    return "conversations";
  }

  /**
   * Creates a conversation (blocking)
   *
   * <p>It is generally not needed to create a conversation explicitly since sending or receiving a
   * message automatically creates a new conversation if it does not already exist between the given
   * app and contact. Creating empty conversation is useful if the metadata of the conversation
   * should be populated when the first message in the conversation is a contact message or the
   * first message in the conversation comes out-of-band and needs to be injected with InjectMessage
   * endpoint.
   *
   * @param conversation The conversation to create. ID will be generated for the conversation and
   *     any ID in the given conversation will be ignored. (required)
   * @return {@link Conversation}
   * @throws ApiException if fails to make API call
   */
  public Conversation create(final Conversation conversation) {
    try {
      return createAsync(conversation).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Creates a conversation
   *
   * <p>It is generally not needed to create a conversation explicitly since sending or receiving a
   * message automatically creates a new conversation if it does not already exist between the given
   * app and contact. Creating empty conversation is useful if the metadata of the conversation
   * should be populated when the first message in the conversation is a contact message or the
   * first message in the conversation comes out-of-band and needs to be injected with InjectMessage
   * endpoint.
   *
   * @param conversation The conversation to create. ID will be generated for the conversation and
   *     any ID in the given conversation will be ignored. (required)
   * @return Async task generating a {@link Conversation}
   */
  public CompletableFuture<Conversation> createAsync(final Conversation conversation) {
    if (conversation == null) {
      return ExceptionUtils.missingParam(PARAM_CONVERSATION);
    }
    return restClient.post(serviceURI, Conversation.class, conversation);
  }

  /**
   * Deletes the conversation together with all the messages sent as part of the conversation.
   * (blocking)
   *
   * @param conversationId The ID of the conversation to be deleted. (required)
   * @throws ApiException if fails to make API call
   */
  public void delete(final String conversationId) {
    try {
      deleteAsync(conversationId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Deletes the conversation together with all the messages sent as part of the conversation.
   *
   * @param conversationId The ID of the conversation to be deleted. (required)
   * @return Async task of the delete call
   */
  public CompletableFuture<Void> deleteAsync(final String conversationId) {
    if (StringUtils.isEmpty(conversationId)) {
      return ExceptionUtils.missingParam(PARAM_CONVERSATION_ID);
    }
    return restClient.delete(withPath(conversationId));
  }

  /**
   * Get a conversation (blocking)
   *
   * <p>A conversation has two participating entities, an app and a contact.
   *
   * @param conversationId The ID of the conversation. (required)
   * @return {@link Conversation}
   * @throws ApiException if fails to make API call
   */
  public Conversation get(final String conversationId) {
    try {
      return getAsync(conversationId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Get a conversation
   *
   * <p>A conversation has two participating entities, an app and a contact.
   *
   * @param conversationId The ID of the conversation. (required)
   * @return Async task generating a {@link Conversation}
   */
  public CompletableFuture<Conversation> getAsync(final String conversationId) {
    if (StringUtils.isEmpty(conversationId)) {
      return ExceptionUtils.missingParam(PARAM_CONVERSATION_ID);
    }
    return restClient.get(withPath(conversationId), Conversation.class);
  }

  /**
   * Inject messages (blocking)
   *
   * <p>This operation injects a conversation conversationMessage in to a specific conversation.
   *
   * <p>The id field of the conversationMessage is ignored and instead generated on the server.
   *
   * @param conversationMessage Message to be injected. (required)
   * @throws ApiException if fails to make API call
   */
  public void injectMessage(final ConversationMessage conversationMessage) {
    try {
      injectMessageAsync(conversationMessage).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Inject messages
   *
   * <p>This operation injects a conversation conversationMessage in to a specific conversation.
   *
   * <p>The id field of the conversationMessage is ignored and instead generated on the server.
   *
   * @param conversationMessage Message to be injected. (required)
   * @return Async task of the inject call
   */
  public CompletableFuture<Void> injectMessageAsync(final ConversationMessage conversationMessage) {
    if (conversationMessage == null) {
      return ExceptionUtils.missingParam(PARAM_CONVERSATION_MESSAGE);
    }
    final String conversationId = conversationMessage.getConversationId();
    if (StringUtils.isEmpty(conversationId)) {
      return ExceptionUtils.missingParam(PARAM_MESSAGE_CONVERSATION_ID);
    }
    if (StringUtils.isEmpty(conversationMessage.getContactId())) {
      return ExceptionUtils.missingParam(PARAM_MESSAGE_CONTACT_ID);
    }
    return restClient.post(withPath(conversationId.concat(":inject-message")), conversationMessage);
  }

  /**
   * List conversations (blocking)
   *
   * <p>This operation lists all conversations that are associated with an app and/or a contact.
   *
   * @param params Object holding the parameters (required)
   * @return {@link V1ListConversationsResponse}
   * @throws ApiException if fails to make API call
   */
  public V1ListConversationsResponse listConversations(final ListConversationsParams params) {
    try {
      return listConversationsAsync(params).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * List conversations
   *
   * <p>This operation lists all conversations that are associated with an app and/or a contact.
   *
   * @param params Object holding the parameters (required)
   * @return Async task generating {@link V1ListConversationsResponse}
   */
  public CompletableFuture<V1ListConversationsResponse> listConversationsAsync(
      final ListConversationsParams params) {
    if (params == null) {
      return ExceptionUtils.missingParam(PARAMS);
    }
    if (!params.isValid()) {
      return ExceptionUtils.missingOneOf(AppService.PARAM_APP_ID, PARAM_CONTACT_ID);
    }
    return restClient.get(withQuery(params.build()), V1ListConversationsResponse.class);
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
  public V1ListMessagesResponse listMessages(final ListMessagesParams params) {
    return messageService.list(params);
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
  public CompletableFuture<V1ListMessagesResponse> listMessagesAsync(
      final ListMessagesParams params) {
    return messageService.listAsync(params);
  }

  /**
   * Stop conversation (blocking)
   *
   * <p>This operation stops the referenced conversation, if the conversation is still active. A new
   * conversation will be created if a new message is exchanged between the app or contact that was
   * part of the stopped conversation.
   *
   * @param conversationId The ID of the conversation to be stopped. (required)
   * @throws ApiException if fails to make API call
   */
  public void stopActive(final String conversationId) {
    try {
      stopActiveAsync(conversationId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Stop conversation
   *
   * <p>This operation stops the referenced conversation, if the conversation is still active. A new
   * conversation will be created if a new message is exchanged between the app or contact that was
   * part of the stopped conversation.
   *
   * @param conversationId The ID of the conversation to be stopped. (required)
   * @return Object
   */
  public CompletableFuture<Void> stopActiveAsync(final String conversationId) {
    if (StringUtils.isEmpty(conversationId)) {
      return ExceptionUtils.missingParam(PARAM_CONVERSATION_ID);
    }
    return restClient.post(withPath(conversationId.concat(":stop")));
  }

  /**
   * Update a conversation (blocking)
   *
   * <p>This operation updates a conversation which can, for instance, be used to update the
   * metadata associated with a conversation.
   *
   * @param conversationId The ID of the conversation. (required)
   * @param conversation The updated conversation. (required)
   * @return {@link Conversation}
   * @throws ApiException if fails to make API call
   */
  public Conversation update(final String conversationId, final Conversation conversation) {
    try {
      return updateAsync(conversationId, conversation).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Update a conversation
   *
   * <p>This operation updates a conversation which can, for instance, be used to update the
   * metadata associated with a conversation.
   *
   * @param conversationId The ID of the conversation. (required)
   * @param conversation The updated conversation. (required)
   * @return Async task generating a {@link Conversation}
   */
  public CompletableFuture<Conversation> updateAsync(
      final String conversationId, final Conversation conversation) {
    if (StringUtils.isEmpty(conversationId)) {
      return ExceptionUtils.missingParam(PARAM_CONVERSATION_ID);
    }
    if (conversation == null) {
      return ExceptionUtils.missingParam(PARAM_CONVERSATION);
    }
    return restClient.patch(withPath(conversationId), Conversation.class, conversation);
  }
}
