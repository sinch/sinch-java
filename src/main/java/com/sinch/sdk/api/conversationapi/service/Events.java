package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.SendEventRequest;
import com.sinch.sdk.model.conversationapi.SendEventResponse;
import com.sinch.sdk.util.ExceptionUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Events extends AbstractService {

  public Events(
      final ConversationApiConfig config, final AuthenticationService authenticationService) {
    super(config, authenticationService);
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
   * @return {@link SendEventResponse}
   * @throws ApiException if fails to make API call
   */
  public SendEventResponse send(final SendEventRequest sendEventRequest) {
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
   * @return Async task generating a {@link SendEventResponse}
   */
  public CompletableFuture<SendEventResponse> sendAsync(final SendEventRequest sendEventRequest) {
    if (sendEventRequest == null) {
      return ExceptionUtils.missingParam("sendEventRequest");
    }
    return restClient.post(
        serviceURI, SendEventResponse.class, sendEventRequest.projectId(projectId));
  }
}
