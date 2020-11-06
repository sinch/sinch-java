package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityResponse;
import javax.validation.Valid;

public interface CapabilityService {
  QueryCapabilityResponse queryCapability(@Valid QueryCapabilityRequest queryCapabilityRequest);
}
