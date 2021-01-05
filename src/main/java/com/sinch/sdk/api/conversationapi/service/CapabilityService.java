package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityResponse;
import javax.validation.Valid;

public class CapabilityService extends ConversationApiService {

  public CapabilityService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "capability:query";
  }

  public QueryCapabilityResponse queryCapability(
      @Valid final QueryCapabilityRequest queryCapabilityRequest) {
    return restClient.post(serviceURI, QueryCapabilityResponse.class, queryCapabilityRequest);
  }
}
