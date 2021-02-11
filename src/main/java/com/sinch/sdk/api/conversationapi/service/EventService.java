package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.V1SendEventRequest;
import com.sinch.sdk.model.conversationapi.V1SendEventResponse;
import com.sinch.sdk.utils.ExceptionUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class EventService extends AbstractService {

  public EventService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "events:send";
  }

  /**
   * Send an event (blocking)
   *
   * <p>Sends an event to the referenced contact from the referenced app. Note that this operation
   * enqueues the event in a queues so a successful response only indicates that the event has been
   * queued.
   *
   * @param sendEventRequest (required)
   * @return {@link V1SendEventResponse}
   * @throws ApiException if fails to make API call
   */
  public V1SendEventResponse send(final V1SendEventRequest sendEventRequest) {
    try {
      return sendAsync(sendEventRequest).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Send an event
   *
   * <p>Sends an event to the referenced contact from the referenced app. Note that this operation
   * enqueues the event in a queues so a successful response only indicates that the event has been
   * queued.
   *
   * @param sendEventRequest (required)
   * @return Async task generating a {@link V1SendEventResponse}
   */
  public CompletableFuture<V1SendEventResponse> sendAsync(
      final V1SendEventRequest sendEventRequest) {
    if (sendEventRequest == null) {
      return ExceptionUtils.missingParam("sendEventRequest");
    }
    return restClient.post(
        serviceURI, V1SendEventResponse.class, sendEventRequest.projectId(projectId));
  }
}
