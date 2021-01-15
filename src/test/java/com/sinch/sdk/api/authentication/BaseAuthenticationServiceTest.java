package com.sinch.sdk.api.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;

import com.sinch.sdk.api.BaseTest;
import com.sinch.sdk.configuration.impl.ConfigurationEU;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BaseAuthenticationServiceTest extends BaseTest {
  protected static final String testClientId = "testClient";
  protected static final String testClientSecret = "testSecret";

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
            OM,
            new ConfigurationEU.AuthenticationEU() {
              @Override
              public long getFallbackRetryDelay() {
                return 1;
              }
            },
            testClientId,
            testClientSecret);
  }

  @SneakyThrows
  protected void thenExpectThatHttpClientSendCalledAtLeast(final int times) {
    Mockito.verify(mockHttpClient, atLeast(times)).send(any(), any());
  }

  @SneakyThrows
  protected void givenServiceWorks() {
    Mockito.lenient()
        .doAnswer(invocation -> getResource("response.json"))
        .when(mockHttpResponse)
        .body();
  }

  @SneakyThrows
  protected void givenServiceKaputski() {
    Mockito.doAnswer(invocation -> getResource("404_response.html")).when(mockHttpResponse).body();
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
