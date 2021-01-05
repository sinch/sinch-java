package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.event.service.SendEventRequest;
import com.sinch.sdk.model.conversationapi.event.service.SendEventResponse;
import javax.validation.Valid;

public class EventService extends AbstractService {

  public EventService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "events:send";
  }

  public SendEventResponse sendEvent(@Valid final SendEventRequest sendEventRequest) {
    return restClient.post(serviceURI, SendEventResponse.class, sendEventRequest);
  }
}
