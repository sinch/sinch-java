package com.sinch.sdk.api.authentication;

import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.test.utils.AwaitUtil;
import java.net.http.HttpTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest extends BaseAuthenticationServiceTest {

  @Test
  void testReloadTokenEveryExpiresIn() {
    AwaitUtil.delaySeconds(3);
    thenExpectThatHttpClientSendCalledAtLeast(3);
  }

  @Test
  void testGetReturnsBearerTokenOnSuccess() {
    Assertions.assertEquals("Bearer access_token", underTest.getHeaderValue());
    thenExpectThatHttpClientSendCalledAtLeast(1);
  }

  @Test
  void testTimeoutThrows() {
    givenSendThrows();
    thenExpectGetHeaderValueTrows(HttpTimeoutException.class);
  }

  @Test
  void testUnauthorizedThrowsConfigurationException() {
    givenUnauthorized();
    thenExpectGetHeaderValueTrows(ConfigurationException.class);
  }

  @Test
  void testTooManyRequestsThrowsAuthorizationException() {
    givenTooManyRequests();
    thenExpectGetHeaderValueTrows(ApiException.class);
  }

  @Test
  void testBasicAuthWorks() {
    givenBasicAuthConfigured();
    Assertions.assertEquals("Basic dGVzdENsaWVudDp0ZXN0U2VjcmV0", underTest.getHeaderValue());
  }
}
