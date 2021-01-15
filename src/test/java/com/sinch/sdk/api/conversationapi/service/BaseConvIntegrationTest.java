package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.BaseTest;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.utils.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;

@Tag("integrationTest")
public class BaseConvIntegrationTest extends BaseTest {

  protected void assertClientSideException(final ApiException exception) {
    Assertions.assertEquals(ExceptionUtils.BAD_REQUEST, exception.getCode());
    Assertions.assertNull(exception.getResponseHeaders());
    Assertions.assertNull(exception.getResponseBody());
  }
}
