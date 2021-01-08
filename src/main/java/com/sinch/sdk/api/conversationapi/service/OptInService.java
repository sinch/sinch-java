package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.optin.OptIn;
import com.sinch.sdk.model.conversationapi.optin.OptOut;
import com.sinch.sdk.model.conversationapi.optin.service.OptInResponse;
import com.sinch.sdk.model.conversationapi.optin.service.OptOutResponse;
import javax.validation.Valid;

public class OptInService extends AbstractService {

  public OptInService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "";
  }

  public OptInResponse registerOptIn(@Valid final OptIn optIn) {
    return restClient.post(withPath("/optins:register"), OptInResponse.class, optIn);
  }

  public OptOutResponse registerOptOut(@Valid final OptOut optOut) {
    return restClient.post(withPath("/optouts:register"), OptOutResponse.class, optOut);
  }
}
