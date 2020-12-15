package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.EventService;
import com.sinch.sdk.model.conversationapi.event.service.SendEventRequest;
import com.sinch.sdk.model.conversationapi.event.service.SendEventResponse;
import javax.validation.Valid;

public class EventServiceImpl extends ConversationApiService implements EventService {

  public EventServiceImpl(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "events:send";
  }

  @Override
  public SendEventResponse sendEvent(@Valid final SendEventRequest sendEventRequest) {
    return restClient.post(serviceURI, SendEventResponse.class, sendEventRequest);
  }
}
