package com.sinch.sdk.api.conversationapi.service;

import static com.sinch.sdk.api.authentication.AuthenticationService.HEADER_KEY_AUTH;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.model.conversationapi.App;
import com.sinch.sdk.restclient.SinchRestClient;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SinchRestClientProxyTest {

  @Mock private AuthenticationService authenticationService;

  @Mock private SinchRestClient restClient;

  @InjectMocks private SinchRestClientProxy sinchRestClientProxy;

  private static final URI DUMMY_URI = URI.create("/");
  private static final String DUMMY_AUTH_TOKEN = "DummyToken";
  private static final Map<String, String> AUTH_HEADER = new HashMap<>();
  private static final Map<String, String> AUTO_ADDED_HEADERS = new HashMap<>();
  private static final Map<String, String> ADDITIONAL_REQUEST_HEADER = new HashMap<>();

  @BeforeAll
  static void setup() {
    ADDITIONAL_REQUEST_HEADER.put("HEADER_1", "HEADER_VALUE_1");
    AUTH_HEADER.put(HEADER_KEY_AUTH, DUMMY_AUTH_TOKEN);
    AUTO_ADDED_HEADERS.putAll(AUTH_HEADER);
    AUTO_ADDED_HEADERS.putAll(SinchRestClientProxy.SDK_VERSION_HEADER);
  }

  @BeforeEach
  void setupAuthService() {
    when(authenticationService.getHeaderValue())
        .thenReturn(CompletableFuture.completedFuture(AUTH_HEADER));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToPostRequest() {
    // when
    sinchRestClientProxy.post(DUMMY_URI);

    // then
    verify(restClient).post(eq(DUMMY_URI), eq(AUTO_ADDED_HEADERS));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToPostRequestWithAdditionalHeaders() {
    // when
    sinchRestClientProxy.post(DUMMY_URI, ADDITIONAL_REQUEST_HEADER);

    // then
    verify(restClient)
        .post(eq(DUMMY_URI), eq(mergeHeaders(ADDITIONAL_REQUEST_HEADER, AUTO_ADDED_HEADERS)));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToGetRequest() {
    // when
    sinchRestClientProxy.get(DUMMY_URI, App.class);

    // then
    verify(restClient).get(eq(DUMMY_URI), eq(App.class), eq(AUTO_ADDED_HEADERS));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToGetRequestWithAdditionalHeaders() {
    // when
    sinchRestClientProxy.get(DUMMY_URI, App.class, ADDITIONAL_REQUEST_HEADER);

    // then
    verify(restClient)
        .get(
            eq(DUMMY_URI),
            eq(App.class),
            eq(mergeHeaders(ADDITIONAL_REQUEST_HEADER, AUTO_ADDED_HEADERS)));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToPatchRequest() {
    // given
    App app = new App().id(RandomStringUtils.randomAlphanumeric(10));

    // when
    sinchRestClientProxy.patch(DUMMY_URI, App.class, app);

    // then
    verify(restClient).patch(eq(DUMMY_URI), eq(App.class), eq(app), eq(AUTO_ADDED_HEADERS));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToPatchRequestWithAdditionalHeaders() {
    // given
    App app = new App().id(RandomStringUtils.randomAlphanumeric(10));

    // when
    sinchRestClientProxy.patch(DUMMY_URI, App.class, app, ADDITIONAL_REQUEST_HEADER);

    // then
    verify(restClient)
        .patch(
            eq(DUMMY_URI),
            eq(App.class),
            eq(app),
            eq(mergeHeaders(ADDITIONAL_REQUEST_HEADER, AUTO_ADDED_HEADERS)));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToDeleteRequest() {
    // when
    sinchRestClientProxy.delete(DUMMY_URI);

    // then
    verify(restClient).delete(eq(DUMMY_URI), eq(AUTO_ADDED_HEADERS));
  }

  @Test
  void shouldAddAuthAndCustomSdkHeaderToDeleteRequestWithAdditionalHeaders() {
    // when
    sinchRestClientProxy.delete(DUMMY_URI, ADDITIONAL_REQUEST_HEADER);

    // then
    verify(restClient)
        .delete(eq(DUMMY_URI), eq(mergeHeaders(ADDITIONAL_REQUEST_HEADER, AUTO_ADDED_HEADERS)));
  }

  private static Map<String, String> mergeHeaders(
      Map<String, String> headers, Map<String, String> headersToAdd) {
    return Stream.concat(headers.entrySet().stream(), headersToAdd.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
