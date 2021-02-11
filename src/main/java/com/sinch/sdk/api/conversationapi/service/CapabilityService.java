package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.V1QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.V1QueryCapabilityResponse;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class CapabilityService extends AbstractService {

  private static final String PARAM_CAPABILITY_REQUEST = "capabilityRequest";
  private static final String PARAM_CAPABILITY_REQUEST_APP_ID =
      PARAM_CAPABILITY_REQUEST + SUB_APP_ID;

  public CapabilityService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "capability:query";
  }

  /**
   * Request a capability lookup (blocking)
   *
   * <p>This method is asynchronous, it immediately returns the requested Capability registration.
   *
   * <p>Capability checks are then delivered as callbacks to registered webhooks with trigger
   * CAPABILITY for every reachable channel.
   *
   * @param capabilityRequest (required)
   * @return {@link V1QueryCapabilityResponse}
   * @throws ApiException if fails to make API call
   */
  public V1QueryCapabilityResponse query(final V1QueryCapabilityRequest capabilityRequest) {
    try {
      return queryAsync(capabilityRequest).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Request a capability lookup
   *
   * <p>This method is asynchronous, it immediately returns the requested Capability registration.
   *
   * <p>Capability checks are then delivered as callbacks to registered webhooks with trigger
   * CAPABILITY for every reachable channel.
   *
   * @param capabilityRequest (required)
   * @return Async task generating a {@link V1QueryCapabilityResponse}
   */
  public CompletableFuture<V1QueryCapabilityResponse> queryAsync(
      final V1QueryCapabilityRequest capabilityRequest) {
    if (capabilityRequest == null) {
      return ExceptionUtils.missingParam(PARAM_CAPABILITY_REQUEST);
    }
    if (StringUtils.isEmpty(capabilityRequest.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_CAPABILITY_REQUEST_APP_ID);
    }
    return restClient.post(
        serviceURI, V1QueryCapabilityResponse.class, capabilityRequest.projectId(projectId));
  }
}
