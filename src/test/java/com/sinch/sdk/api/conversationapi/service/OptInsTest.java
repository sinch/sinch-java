package com.sinch.sdk.api.conversationapi.service;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;

import com.sinch.sdk.api.conversationapi.factory.RecipientFactory;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.OptIn;
import com.sinch.sdk.model.conversationapi.OptInResponse;
import com.sinch.sdk.model.conversationapi.OptOut;
import com.sinch.sdk.model.conversationapi.OptOutResponse;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class OptInsTest extends BaseServiceTest {

  private static final String CONTACT_ID = "contact-id";
  private static final String APP_ID = "app-id";

  private static OptIns optIns;

  @BeforeEach
  void setUp() {
    optIns = new OptIns(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final OptIns optIns = new OptIns(CONFIG, null);
    assertThat(optIns.restClient).isNotNull();
    assertThat(optIns.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, optIns.getServiceName()));
  }

  @Test
  void registerOptIn() {
    optIns.optIn(
        new OptIn()
            .appId(APP_ID)
            .addChannelsItem(ConversationChannel.WHATSAPP)
            .recipient(RecipientFactory.fromContactId(CONTACT_ID)),
        null);

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(PROJECT_ID + "/" + OptIns.OPTINS_REGISTER),
        OptInResponse.class,
        () -> isA(OptIn.class));
  }

  @Test
  void registerOptOut() {
    optIns.optOut(
        new OptOut()
            .appId(APP_ID)
            .addChannelsItem(ConversationChannel.WHATSAPP)
            .recipient(RecipientFactory.fromContactId(CONTACT_ID)),
        null);

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(PROJECT_ID + "/" + OptIns.OPTOUTS_REGISTER),
        OptOutResponse.class,
        () -> isA(OptOut.class));
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(final ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowingCallable> callsWithMissingParams() {
    return Arrays.asList(
        () -> optIns.optIn(null, null),
        () -> optIns.optIn(new OptIn().appId(APP_ID), null),
        () -> optIns.optIn(new OptIn().channels(emptyList()), null),
        () -> optIns.optOut(null, null),
        () -> optIns.optOut(new OptOut().appId(APP_ID), null),
        () -> optIns.optOut(new OptOut().channels(emptyList()), null));
  }
}
