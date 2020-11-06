package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.event.service.SendEventRequest;
import com.sinch.sdk.model.conversationapi.event.service.SendEventResponse;
import javax.validation.Valid;

public interface EventService {

  SendEventResponse sendEvent(@Valid SendEventRequest sendEventRequest);
}
