package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.CapabilityService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityResponse;
import java.util.function.Supplier;
import javax.validation.Valid;

public class CapabilityServiceImpl extends ConversationApiService implements CapabilityService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s/capability:query";

  public CapabilityServiceImpl(ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public QueryCapabilityResponse queryCapability(
      @Valid QueryCapabilityRequest queryCapabilityRequest) {
    return postRequest("", QueryCapabilityResponse.class, queryCapabilityRequest);
  }
}
