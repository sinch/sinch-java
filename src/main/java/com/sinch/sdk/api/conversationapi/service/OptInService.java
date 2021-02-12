package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.OptIn;
import com.sinch.sdk.model.conversationapi.OptInResponse;
import com.sinch.sdk.model.conversationapi.OptOut;
import com.sinch.sdk.model.conversationapi.OptOutResponse;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.QueryStringBuilder;
import com.sinch.sdk.utils.StringUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@SuppressWarnings("DuplicatedCode")
public class OptInService extends AbstractService {

  private static final String PARAM_OPT_IN = "optIn";
  private static final String PARAM_IN_APP_ID = PARAM_OPT_IN + SUB_APP_ID;
  private static final String PARAM_IN_CHANNELS = PARAM_OPT_IN + SUB_CHANNELS;
  private static final String PARAM_OPT_OUT = "optOut";
  private static final String PARAM_OUT_APP_ID = PARAM_OPT_OUT + SUB_APP_ID;
  private static final String PARAM_OUT_CHANNELS = PARAM_OPT_OUT + SUB_CHANNELS;

  public OptInService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "";
  }

  /**
   * Register an opt-in (blocking)
   *
   * <p>This method is asynchronous - it returns immediately the requested OptIn registration with
   * any defaults resolved. All changes of the actual opt-in status are then delivered as callbacks
   * to registered webhooks with trigger OPT_IN.
   *
   * @param optIn (required)
   * @param requestId ID for the asynchronous request, will be generated if not set. (optional)
   * @return {@link OptInResponse}
   * @throws ApiException if fails to make API call
   */
  public OptInResponse optIn(final OptIn optIn, final String requestId) {
    try {
      return optInAsync(optIn, requestId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Register an opt-in
   *
   * <p>This method is asynchronous - it returns immediately the requested OptIn registration with
   * any defaults resolved. All changes of the actual opt-in status are then delivered as callbacks
   * to registered webhooks with trigger OPT_IN.
   *
   * @param optIn (required)
   * @param requestId ID for the asynchronous request, will be generated if not set. (optional)
   * @return Async task generating a {@link OptInResponse}
   */
  public CompletableFuture<OptInResponse> optInAsync(final OptIn optIn, final String requestId) {
    if (optIn == null) {
      return ExceptionUtils.missingParam(PARAM_OPT_IN);
    }
    if (StringUtils.isEmpty(optIn.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_IN_APP_ID);
    }
    if (optIn.getChannels() == null) {
      return ExceptionUtils.missingParam(PARAM_IN_CHANNELS);
    }
    final String queryString =
        QueryStringBuilder.newInstance()
            .add(OptInResponse.JSON_PROPERTY_REQUEST_ID, requestId)
            .build();
    return restClient.post(
        withQuery("optins:register".concat(queryString)), OptInResponse.class, optIn);
  }

  /**
   * Register an opt-out (blocking)
   *
   * <p>This method is asynchronous - it returns immediately the requested OptOut registration with
   * any defaults resolved. All changes of the actual opt-out status are then delivered as callbacks
   * to registered webhooks with trigger OPT_OUT.
   *
   * @param optOut (required)
   * @param requestId ID for the asynchronous request, will be generated if not set. (optional)
   * @return {@link OptOutResponse}
   * @throws ApiException if fails to make API call
   */
  public OptOutResponse optOut(final OptOut optOut, final String requestId) {
    try {
      return optOutAsync(optOut, requestId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Register an opt-out
   *
   * <p>This method is asynchronous - it returns immediately the requested OptOut registration with
   * any defaults resolved. All changes of the actual opt-out status are then delivered as callbacks
   * to registered webhooks with trigger OPT_OUT.
   *
   * @param optOut (required)
   * @param requestId ID for the asynchronous request, will be generated if not set. (optional)
   * @return Async task generating a {@link OptOutResponse}
   */
  public CompletableFuture<OptOutResponse> optOutAsync(
      final OptOut optOut, final String requestId) {
    if (optOut == null) {
      return ExceptionUtils.missingParam(PARAM_OPT_OUT);
    }
    if (StringUtils.isEmpty(optOut.getAppId())) {
      return ExceptionUtils.missingParam(PARAM_OUT_APP_ID);
    }
    if (optOut.getChannels() == null) {
      return ExceptionUtils.missingParam(PARAM_OUT_CHANNELS);
    }
    final String queryString =
        QueryStringBuilder.newInstance()
            .add(OptOutResponse.JSON_PROPERTY_REQUEST_ID, requestId)
            .build();
    return restClient.post(
        withQuery("optouts:register".concat(queryString)), OptOutResponse.class, optOut);
  }
}
