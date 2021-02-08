package com.sinch.sdk.api.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;

import com.sinch.sdk.api.BaseTest;
import com.sinch.sdk.configuration.impl.ConfigurationEU;
import com.sinch.sdk.test.utils.AwaitUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.ExecutionException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BaseAuthenticationServiceTest extends BaseTest {
  protected static final String TEST_ID = "testClient";
  protected static final String TEST_SECRET = "testSecret";

  @Mock private HttpClient mockHttpClient;
  @Mock private HttpResponse<InputStream> mockHttpResponse;

  protected AuthenticationService underTest;

  @BeforeEach
  void setUp() throws IOException, InterruptedException {
    givenServiceWorks();
    Mockito.lenient()
        .doReturn(mockHttpResponse)
        .when(mockHttpClient)
        .send(
            argThat(
                arg ->
                    arg.method().equalsIgnoreCase("POST")
                        && arg.headers()
                            .map()
                            .get(AuthenticationService.HEADER_KEY_AUTH)
                            .contains("Basic dGVzdENsaWVudDp0ZXN0U2VjcmV0")),
            ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any());

    underTest =
        new AuthenticationService(
            mockHttpClient,
            new ConfigurationEU.AuthenticationEU() {
              @Override
              public long getFallbackRetryDelay() {
                return 1;
              }
            },
            TEST_ID,
            TEST_SECRET);
  }

  @SneakyThrows
  protected void thenExpectThatHttpClientSendCalledAtLeast(final int times) {
    Mockito.verify(mockHttpClient, atLeast(times)).send(any(), any());
  }

  protected <T extends Throwable> void thenExpectGetHeaderValueTrows(
      final Class<T> expectedException) {
    AwaitUtil.awaitValidAssertion(
        () -> {
          final ExecutionException exception =
              Assertions.assertThrows(ExecutionException.class, () -> underTest.getHeaderValue());
          Assertions.assertTrue(expectedException.isInstance(exception.getCause()));
        });
  }

  protected void givenBasicAuthConfigured() {
    underTest =
        new AuthenticationService(
            mockHttpClient,
            new ConfigurationEU.AuthenticationEU() {
              @Override
              public long getFallbackRetryDelay() {
                return 1;
              }

              @Override
              public boolean useBasicAuth() {
                return true;
              }
            },
            TEST_ID,
            TEST_SECRET);
  }

  protected void givenServiceWorks() {
    Mockito.lenient()
        .doAnswer(invocation -> getResource("response.json"))
        .when(mockHttpResponse)
        .body();
    Mockito.lenient().doReturn(200).when(mockHttpResponse).statusCode();
  }

  protected void givenUnauthorized() {
    Mockito.doAnswer(invocation -> getResource("401_response.json")).when(mockHttpResponse).body();
    Mockito.doReturn(401).when(mockHttpResponse).statusCode();
  }

  protected void givenTooManyRequests() {
    AwaitUtil.delaySeconds(0); // Needed to delay mock slightly
    Mockito.lenient().doReturn(429).when(mockHttpResponse).statusCode();
  }

  @SneakyThrows
  protected void givenSendThrows() {
    Mockito.doThrow(HttpTimeoutException.class).when(mockHttpClient).send(any(), any());
  }

  @Override
  protected InputStream getResource(final String fileName) {
    return super.getResource("authentication/" + fileName);
  }
}
