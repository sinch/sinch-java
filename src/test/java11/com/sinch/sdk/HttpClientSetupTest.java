package com.sinch.sdk;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.authentication.model.AuthResponse;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.App;
import com.sinch.sdk.restclient.ApacheHttpRestClientFactory;
import com.sinch.sdk.restclient.JavaRestClientFactory;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import com.sinch.sdk.restclient.SinchRestClientFactory;
import com.sinch.sdk.test.extension.WireMockExtension;
import com.sinch.sdk.test.extension.WireMockExtension.WiremockUri;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(WireMockExtension.class)
class HttpClientSetupTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @ParameterizedTest
  @MethodSource("httpClientConfigs")
  void httpLayer(TestConfig testConfig, @WiremockUri String serverUri) throws Exception {
    String authPath = "/auth";
    System.setProperty("sinch.authentication.url", serverUri + authPath);
    System.setProperty("sinch.conversationApi.url", serverUri);
    String projectId = RandomStringUtils.randomAlphanumeric(10);
    stubFor(
        get(urlEqualTo(String.format("/v1/projects/%s/apps", projectId)))
            .willReturn(
                aResponse().withBody(OBJECT_MAPPER.writeValueAsBytes(new App().id("appId")))));
    stubFor(
        post(urlEqualTo(authPath))
            .willReturn(
                aResponse()
                    .withBody(OBJECT_MAPPER.writeValueAsBytes(AuthResponse.builder().build()))));

    Class<?> testClass =
        isolatedClassLoader(testConfig.toFilter).loadClass(testConfig.testClass.getName());
    Method testMethod = testClass.getMethod("test", String.class);
    if (testConfig.shouldFail) {
      try {
        testMethod.invoke(null, projectId);
        Assertions.fail("Class should be missing in runtime and test should fail");
      } catch (Exception e) {
        // correct
      }
    } else {
      testMethod.invoke(null, projectId);
    }
  }

  static Stream<TestConfig> httpClientConfigs() {
    return Stream.of(
        new TestConfig(OkHttpApplication.class, List.of("httpclient"), false),
        new TestConfig(ApacheHttpClientApplication.class, List.of("okhttp"), false),
        new TestConfig(JdkHttpClientApplication.class, List.of("okhttp", "httpclient"), false),
        new TestConfig(OkHttpApplication.class, List.of("okhttp"), true),
        new TestConfig(ApacheHttpClientApplication.class, List.of("httpclient"), true));
  }

  static ClassLoader isolatedClassLoader(List<String> toFilter) {
    String classpath = System.getProperty("java.class.path");
    URL[] urls =
        Arrays.stream(classpath.split(":"))
            .filter(entry -> toFilter.stream().noneMatch(entry::contains))
            .map(HttpClientSetupTest::createFileURL)
            .toArray(URL[]::new);
    return new URLClassLoader(urls, ClassLoader.getSystemClassLoader().getParent());
  }

  @SneakyThrows
  private static URL createFileURL(String classpathEntry) {
    return new URL(
        "file:"
            + classpathEntry
            + (classpathEntry.endsWith("/") || classpathEntry.endsWith(".jar") ? "" : "/"));
  }

  public static class OkHttpApplication {

    public static void test(String projectId) {
      HttpClientSetupTest.test(projectId, new OkHttpRestClientFactory(new OkHttpClient()));
    }
  }

  public static class ApacheHttpClientApplication {

    @SneakyThrows
    public static void test(String projectId) {
      try (final CloseableHttpAsyncClient httpClient = HttpAsyncClientBuilder.create().build()) {
        httpClient.start();
        HttpClientSetupTest.test(projectId, new ApacheHttpRestClientFactory(httpClient));
      }
    }
  }

  public static class JdkHttpClientApplication {

    public static void test(String projectId) {
      HttpClientSetupTest.test(projectId, new JavaRestClientFactory(HttpClient.newHttpClient()));
    }
  }

  public static void test(String projectId, SinchRestClientFactory restClientFactorySupplier) {
    Sinch.init("", "", projectId);
    Sinch.conversationApi(Region.EU, () -> restClientFactorySupplier).apps().list();
  }

  static class TestConfig {

    private final Class<?> testClass;
    private final List<String> toFilter;
    private final boolean shouldFail;

    TestConfig(Class<?> testClass, List<String> toFilter, boolean shouldFail) {
      this.toFilter = toFilter;
      this.testClass = testClass;
      this.shouldFail = shouldFail;
    }
  }
}
