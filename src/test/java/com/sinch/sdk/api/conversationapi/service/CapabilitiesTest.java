package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

import com.sinch.sdk.model.conversationapi.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.QueryCapabilityResponse;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CapabilitiesTest extends BaseServiceTest {

  private static Capabilities capabilities;

  @BeforeEach
  void setUp() {
    capabilities = new Capabilities(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final Capabilities capabilities = new Capabilities(CONFIG, null);
    assertThat(capabilities.restClient).isNotNull();
    assertThat(capabilities.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, capabilities.getServiceName()));
  }

  @Test
  void queryCapability() {
    final String appId = "app-id";
    capabilities.query(new QueryCapabilityRequest().appId(appId));

    verifyPostCalled(
        () -> eq(capabilities.serviceURI),
        QueryCapabilityResponse.class,
        () -> argThat((QueryCapabilityRequest req) -> PROJECT_ID.equals(req.getProjectId())));
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(final ThrowableAssert.ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowableAssert.ThrowingCallable> callsWithMissingParams() {
    return Arrays.asList(
        () -> capabilities.query(null), () -> capabilities.query(new QueryCapabilityRequest()));
  }
}
