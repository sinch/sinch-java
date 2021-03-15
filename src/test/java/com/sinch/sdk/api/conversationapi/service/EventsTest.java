package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

import com.sinch.sdk.model.conversationapi.SendEventRequest;
import com.sinch.sdk.model.conversationapi.SendEventResponse;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EventsTest extends BaseServiceTest {

  private static Events events;

  @BeforeEach
  void setUp() {
    events = new Events(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final Events events = new Events(CONFIG, null);
    assertThat(events.restClient).isNotNull();
    assertThat(events.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, events.getServiceName()));
  }

  @Test
  void sendEvent() {
    events.send(new SendEventRequest());

    verifyPostCalled(
        () -> eq(events.serviceURI),
        SendEventResponse.class,
        () -> argThat((SendEventRequest req) -> PROJECT_ID.equals(req.getProjectId())));
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowingCallable> callsWithMissingParams() {
    return Collections.singletonList(() -> events.send(null));
  }
}
