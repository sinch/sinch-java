package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.OptInService;
import com.sinch.sdk.model.conversationapi.optin.OptIn;
import com.sinch.sdk.model.conversationapi.optin.OptOut;
import com.sinch.sdk.model.conversationapi.optin.service.OptInResponse;
import com.sinch.sdk.model.conversationapi.optin.service.OptOutResponse;
import java.util.function.Supplier;
import javax.validation.Valid;

public class OptInServiceImpl extends ConversationApiService implements OptInService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s";
  private static final String OPT_IN_PATH = "/optins:register";
  private static final String OPT_OUT_PATH = "/optouts:register";

  public OptInServiceImpl(ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public OptInResponse registerOptIn(@Valid OptIn optIn) {
    return postRequest(OPT_IN_PATH, OptInResponse.class, optIn);
  }

  @Override
  public OptOutResponse registerOptOut(@Valid OptOut optOut) {
    return postRequest(OPT_OUT_PATH, OptOutResponse.class, optOut);
  }
}
