package com.sinch.sdk.extensions;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class WireMockExtension
    implements ParameterResolver, BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

  private WireMockServer wireMockServer;

  @Override
  public void afterAll(ExtensionContext context) {
    wireMockServer.stop();
    wireMockServer = null;
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    wireMockServer = new WireMockServer();
    wireMockServer.start();
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    wireMockServer.resetAll();
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(WiremockUri.class)
        && String.class.isAssignableFrom(parameterContext.getParameter().getType());
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return wireMockServer.baseUrl();
  }

  @Target({ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface WiremockUri {}
}
