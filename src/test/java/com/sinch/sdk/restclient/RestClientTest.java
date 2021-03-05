package com.sinch.sdk.restclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.model.conversationapi.App;
import com.sinch.sdk.test.extension.WireMockExtension;
import com.sinch.sdk.test.extension.WireMockExtension.WiremockUri;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(WireMockExtension.class)
class RestClientTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String HEADER_NAME = "Content-Type";
  private static final String HEADER_VALUE = "application/json";
  private static final Map<String, String> HEADER_MAP = new HashMap<>();

  @BeforeAll
  static void setup() {
    HEADER_MAP.put(HEADER_NAME, HEADER_VALUE);
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldReturnAppWhenGetReturnsOk(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    App app = randomApp();
    String appUrl = "/apps/" + app.getId();
    stubFor(
        get(urlEqualTo("/apps/" + app.getId()))
            .willReturn(
                aResponse().withStatus(HTTP_OK).withBody(OBJECT_MAPPER.writeValueAsBytes(app))));

    assertThat(restClient.get(URI.create(wiremockUri + appUrl), App.class).join())
        .extracting(App::getId)
        .isEqualTo(app.getId());
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldReturnConfigurationExceptionWhenGetReturnsUnauthorized(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    String appId = randomAlphabetic(10);
    String appUrl = "/apps/" + appId;
    stubFor(
        get(urlEqualTo("/apps/" + appId)).willReturn(aResponse().withStatus(HTTP_UNAUTHORIZED)));

    Throwable throwable =
        catchThrowable(() -> restClient.get(URI.create(wiremockUri + appUrl), App.class).join());
    assertThat(throwable)
        .isInstanceOf(CompletionException.class)
        .extracting(Throwable::getCause)
        .isInstanceOf(ConfigurationException.class);
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldReturnApiExceptionWhenGetReturnsServerError(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    String appId = randomAlphabetic(10);
    String appUrl = "/apps/" + appId;
    stubFor(
        get(urlEqualTo("/apps/" + appId)).willReturn(aResponse().withStatus(HTTP_INTERNAL_ERROR)));

    Throwable throwable =
        catchThrowable(() -> restClient.get(URI.create(wiremockUri + appUrl), App.class).join());
    assertThat(throwable)
        .isInstanceOf(CompletionException.class)
        .extracting(Throwable::getCause)
        .isInstanceOf(ApiException.class);
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldReturnAppWhenGetWithHeadersIsCalled(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    App app = randomApp();
    String appUrl = "/apps/" + app.getId();
    stubFor(
        get(urlEqualTo("/apps/" + app.getId()))
            .withHeader(HEADER_NAME, equalTo(HEADER_VALUE))
            .willReturn(
                aResponse().withStatus(HTTP_OK).withBody(OBJECT_MAPPER.writeValueAsBytes(app))));

    assertThat(restClient.get(URI.create(wiremockUri + appUrl), App.class, HEADER_MAP).join())
        .extracting(App::getId)
        .isEqualTo(app.getId());
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldCreateAppAndReturnNothingWhenPostIsCalled(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    App app = randomApp();
    String appsUrl = "/apps";
    stubFor(
        post(urlEqualTo(appsUrl))
            .willReturn(
                aResponse()
                    .withStatus(HTTP_CREATED)
                    .withBody(OBJECT_MAPPER.writeValueAsBytes(app))));

    assertThat(restClient.post(URI.create(wiremockUri + appsUrl)).isCompletedExceptionally())
        .isFalse();
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldCreateAppAndReturnNothingWhenPostIsCalledWithHeaders(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    App app = randomApp();
    String appsUrl = "/apps";
    stubFor(
        post(urlEqualTo(appsUrl))
            .withHeader(HEADER_NAME, equalTo(HEADER_VALUE))
            .willReturn(
                aResponse()
                    .withStatus(HTTP_CREATED)
                    .withBody(OBJECT_MAPPER.writeValueAsBytes(app))));

    assertThat(
            restClient
                .post(URI.create(wiremockUri + appsUrl), HEADER_MAP)
                .isCompletedExceptionally())
        .isFalse();
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldCreateAppWhenPostIsCalled(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    App app = randomApp();
    String appsUrl = "/apps";
    stubFor(
        post(urlEqualTo(appsUrl))
            .willReturn(
                aResponse()
                    .withStatus(HTTP_CREATED)
                    .withBody(OBJECT_MAPPER.writeValueAsBytes(app))));

    assertThat(restClient.post(URI.create(wiremockUri + appsUrl), App.class, app).join())
        .extracting(App::getId)
        .isEqualTo(app.getId());
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldUpdateAppWhenPatchIsCalled(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    App app = randomApp();
    String appUrl = "/apps/" + app.getId();
    stubFor(
        patch(urlEqualTo(appUrl))
            .willReturn(
                aResponse().withStatus(HTTP_OK).withBody(OBJECT_MAPPER.writeValueAsBytes(app))));

    assertThat(restClient.patch(URI.create(wiremockUri + appUrl), App.class, app).join())
        .extracting(App::getId)
        .isEqualTo(app.getId());
  }

  @ParameterizedTest
  @MethodSource("restClients")
  @SneakyThrows
  void shouldDeleteAppWhenDeleteIsCalled(
      SinchRestClient restClient, @WiremockUri String wiremockUri) {
    App app = randomApp();
    String appUrl = "/apps/" + app.getId();
    stubFor(delete(urlEqualTo(appUrl)).willReturn(aResponse().withStatus(HTTP_OK)));

    assertThat(restClient.delete(URI.create(wiremockUri + appUrl)).isCompletedExceptionally())
        .isFalse();
  }

  private App randomApp() {
    return new App().id(randomAlphabetic(10));
  }

  private static List<SinchRestClient> restClients() {
    final CloseableHttpAsyncClient httpClient = HttpAsyncClientBuilder.create().build();
    httpClient.start();
    return Arrays.asList(
        new OkHttpRestClientFactory(new OkHttpClient()).getClient(null, OBJECT_MAPPER),
        new ApacheHttpRestClientFactory(httpClient).getClient(null, OBJECT_MAPPER));
  }
}
