package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.EventService;
import com.sinch.sdk.model.conversationapi.event.service.SendEventRequest;
import com.sinch.sdk.model.conversationapi.event.service.SendEventResponse;
import java.util.function.Supplier;
import javax.validation.Valid;

public class EventServiceImpl extends ConversationApiService implements EventService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s/events:send";

  public EventServiceImpl(ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public SendEventResponse sendEvent(@Valid SendEventRequest sendEventRequest) {
    return postRequest("", SendEventResponse.class, sendEventRequest);
  }
}
