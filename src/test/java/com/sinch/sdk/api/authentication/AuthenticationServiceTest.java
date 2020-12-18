package com.sinch.sdk.api.authentication;

import com.sinch.sdk.test.utils.AwaitUtil;
import org.awaitility.core.ThrowingRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest extends BaseAuthenticationServiceTest {

  private final ThrowingRunnable assertBasicHeader =
      () ->
          Assertions.assertEquals("Basic dGVzdENsaWVudDp0ZXN0U2VjcmV0", underTest.getHeaderValue());

  private final ThrowingRunnable assertBearerHeader =
      () -> Assertions.assertEquals("Bearer access_token", underTest.getHeaderValue());

  @Test
  void testReloadTokenEveryExpiresIn() {
    AwaitUtil.delaySeconds(3);
    thenExpectThatHttpClientSendCalled(3);
  }

  @Test
  void testGetReturnsBearerTokenOnSuccess() throws Throwable {
    assertBearerHeader.run();
    thenExpectThatHttpClientSendCalled(1);
  }

  @Test
  void testExceptionGivesBasicAuth() {
    givenSendThrows();
    AwaitUtil.awaitValidAssertion(assertBasicHeader);
  }

  @Test
  void testKapuskiServiceGivesBasicAuth() {
    givenServiceKaputski();
    AwaitUtil.awaitValidAssertion(assertBasicHeader);
  }

  @Test
  void testFailedRequestStillRetries() {
    givenServiceKaputski();
    AwaitUtil.awaitValidAssertion(assertBasicHeader);
    givenServiceWorks();
    AwaitUtil.awaitValidAssertion(assertBearerHeader);
  }
}
