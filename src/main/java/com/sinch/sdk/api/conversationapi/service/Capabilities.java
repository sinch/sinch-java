package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.QueryCapabilityResponse;
import com.sinch.sdk.util.ExceptionUtils;
import com.sinch.sdk.util.StringUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Capabilities extends AbstractService {

  private static final String PARAM_CAPABILITY_REQUEST = "capabilityRequest";
  private static final String PARAM_CAPABILITY_REQUEST_APP_ID =
      PARAM_CAPABILITY_REQUEST + SUB_APP_ID;

  public Capabilities(
      final ConversationApiConfig config, final AuthenticationService authenticationService) {
    super(config, authenticationService);
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
   * @return {@link QueryCapabilityResponse}
   * @throws ApiException if fails to make API call
   */
  public QueryCapabilityResponse query(final QueryCapabilityRequest capabilityRequest) {
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
   * @return Async task generating a {@link QueryCapabilityResponse}
   */
  public CompletableFuture<QueryCapabilityResponse> queryAsync(
      final QueryCapabilityRequest capabilityRequest) {
    if (capabilityRequest == null) {
      return ExceptionUtils.missingParam(PARAM_CAPABILITY_REQUEST);
    }
    if (StringUtils.isEmpty(capabilityRequest.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_CAPABILITY_REQUEST_APP_ID);
    }
    return restClient.post(
        serviceURI, QueryCapabilityResponse.class, capabilityRequest.projectId(projectId));
  }
}
