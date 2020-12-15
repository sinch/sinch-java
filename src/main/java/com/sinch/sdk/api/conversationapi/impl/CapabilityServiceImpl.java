package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.CapabilityService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityResponse;
import javax.validation.Valid;

public class CapabilityServiceImpl extends ConversationApiService implements CapabilityService {

  public CapabilityServiceImpl(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "capability:query";
  }

  @Override
  public QueryCapabilityResponse queryCapability(
      @Valid final QueryCapabilityRequest queryCapabilityRequest) {
    return restClient.post(serviceURI, QueryCapabilityResponse.class, queryCapabilityRequest);
  }
}
