package com.sinch.sdk.api.authentication;

import static com.sinch.sdk.api.authentication.AuthenticationService.HEADER_KEY_AUTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sinch.sdk.configuration.impl.ConfigurationEU;
import com.sinch.sdk.configuration.impl.ConfigurationEU.AuthenticationEU;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.extensions.ResourceExtension;
import com.sinch.sdk.extensions.ResourceExtension.Resource;
import com.sinch.sdk.model.common.auth.service.AuthResponse;
import com.sinch.sdk.restclient.SinchRestClient;
import com.sinch.sdk.test.utils.AwaitUtil;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ResourceExtension.class)
class AuthenticationServiceTest {

  private final String TEST_ID = "testClient";
  private final String TEST_SECRET = "testSecret";

  @Mock private SinchRestClient authRestClientMock;

  @Test
  void shouldReloadTokenThreeTimesInThreeSecondsWhenExpiresIsOneSecond() {
    // given
    when(authRestClientMock.post(any(), eq(AuthResponse.class), any(), any()))
        .thenReturn(CompletableFuture.completedFuture(AuthResponse.builder().build()));

    // when
    new AuthenticationService(
        authRestClientMock,
        new AuthenticationEU() {
          @Override
          public long getFallbackRetryDelay() {
            return 1;
          }
        },
        TEST_ID,
        TEST_SECRET);
    AwaitUtil.delaySeconds(3);

    // then
    verify(authRestClientMock, atLeast(3)).post(any(), eq(AuthResponse.class), any(), any());
  }

  @Test
  void shouldReturnBearerTokenWhenAuthResponseIsSuccessful(
      @Resource(value = "authentication/response.json", type = AuthResponse.class)
          AuthResponse authResponse) {
    // given
    when(authRestClientMock.post(any(), eq(AuthResponse.class), any(), any()))
        .thenReturn(CompletableFuture.completedFuture(authResponse));
    AuthenticationService authenticationService =
        new AuthenticationService(authRestClientMock, new AuthenticationEU(), TEST_ID, TEST_SECRET);

    // when
    String authTokenValue = authenticationService.getHeaderValue().join().get(HEADER_KEY_AUTH);

    // then
    assertThat(authTokenValue).isEqualTo("Bearer access_token");
    verify(authRestClientMock, atLeast(1)).post(any(), eq(AuthResponse.class), any(), any());
  }

  @Test
  void shouldReturnWrappedExceptionThrowsWhenCallToAuthServiceReturnsException() {
    // given
    final String exceptionMessage = "Wrapped exception";
    final CompletableFuture<AuthResponse> failedFuture = new CompletableFuture<>();
    failedFuture.completeExceptionally(new RuntimeException(exceptionMessage));

    when(authRestClientMock.post(any(), eq(AuthResponse.class), any(), any()))
        .thenReturn(failedFuture);
    AuthenticationService authenticationService =
        new AuthenticationService(authRestClientMock, new AuthenticationEU(), TEST_ID, TEST_SECRET);

    // when
    Throwable throwable = catchThrowable(() -> authenticationService.getHeaderValue().join());

    // then
    assertThat(throwable).isInstanceOf(CompletionException.class);
    assertThat(throwable.getCause())
        .isInstanceOf(RuntimeException.class)
        .extracting(Throwable::getMessage)
        .isEqualTo(exceptionMessage);
  }

  @Test
  void shouldReturnUnauthorizedWhenAuthRestClientReturnsUnauthorized(
      @Resource("authentication/401_response.json") String authResponse) {
    // given
    final CompletableFuture<AuthResponse> failedFuture = new CompletableFuture<>();
    failedFuture.completeExceptionally(
        new ConfigurationException(
            "Invalid credentials, verify the keyId and keySecret", authResponse));
    when(authRestClientMock.post(any(), eq(AuthResponse.class), any(), any()))
        .thenReturn(failedFuture);
    AuthenticationService authenticationService =
        new AuthenticationService(authRestClientMock, new AuthenticationEU(), TEST_ID, TEST_SECRET);

    // when
    Throwable throwable = catchThrowable(() -> authenticationService.getHeaderValue().join());

    // then
    assertThat(throwable).isInstanceOf(CompletionException.class);
    assertThat(throwable.getCause()).isInstanceOf(ConfigurationException.class);
  }

  @Test
  void shouldReturnWrappedTooManyRequestsWhenThrowsByAuthRestClient() {
    // given
    final CompletableFuture<AuthResponse> failedFuture = new CompletableFuture<>();
    failedFuture.completeExceptionally(new ApiException(429, ""));
    when(authRestClientMock.post(any(), eq(AuthResponse.class), any(), any()))
        .thenReturn(failedFuture);
    AuthenticationService authenticationService =
        new AuthenticationService(authRestClientMock, new AuthenticationEU(), TEST_ID, TEST_SECRET);

    // when
    Throwable throwable = catchThrowable(() -> authenticationService.getHeaderValue().join());

    // then
    assertThat(throwable).isInstanceOf(CompletionException.class);
    assertThat(throwable.getCause()).isInstanceOf(ApiException.class);
  }

  @Test
  void shouldReturnBasicHeaderWhenConfigured() {
    // when
    AuthenticationService authenticationService =
        new AuthenticationService(
            authRestClientMock,
            new ConfigurationEU.AuthenticationEU() {
              @Override
              public boolean useBasicAuth() {
                return true;
              }
            },
            TEST_ID,
            TEST_SECRET);

    // when
    String authHeaderValue = authenticationService.getHeaderValue().join().get(HEADER_KEY_AUTH);

    // then
    assertThat(authHeaderValue).isEqualTo("Basic dGVzdENsaWVudDp0ZXN0U2VjcmV0");
  }
}
