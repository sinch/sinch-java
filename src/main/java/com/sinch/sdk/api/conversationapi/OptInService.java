package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.optin.OptIn;
import com.sinch.sdk.model.conversationapi.optin.OptOut;
import com.sinch.sdk.model.conversationapi.optin.service.OptInResponse;
import com.sinch.sdk.model.conversationapi.optin.service.OptOutResponse;
import javax.validation.Valid;

public interface OptInService {

  OptInResponse registerOptIn(@Valid OptIn optIn);

  OptOutResponse registerOptOut(@Valid OptOut optOut);
}
