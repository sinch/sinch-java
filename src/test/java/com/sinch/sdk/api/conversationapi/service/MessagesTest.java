package com.sinch.sdk.api.conversationapi.service;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;

import com.sinch.sdk.api.conversationapi.model.ListMessagesParams;
import com.sinch.sdk.api.conversationapi.model.request.message.TextMessageRequest;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.ConversationMessage;
import com.sinch.sdk.model.conversationapi.ListMessagesResponse;
import com.sinch.sdk.model.conversationapi.SendMessageRequest;
import com.sinch.sdk.model.conversationapi.SendMessageResponse;
import com.sinch.sdk.model.conversationapi.TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.TranscodeMessageResponse;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class MessagesTest extends BaseServiceTest {

  private static final String APP_ID = "app-id";
  private static final String MESSAGE_ID = "message-id";

  private static Messages messages;

  @BeforeEach
  void setUp() {
    messages = getMessages(null);
  }

  @Test
  void publicConstructor() {
    new Messages(CONFIG, null);
    final Messages messages = new Messages(CONFIG, null, null);
    assertThat(messages.restClient).isNotNull();
    assertThat(messages.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, messages.getServiceName()));
  }

  @Test
  void deleteMessage() {
    messages.delete(MESSAGE_ID);

    verifyDeleteCalled(() -> uriPathEndsWithMatcher(MESSAGE_ID));
  }

  @Test
  void getMessage() {
    messages.get(MESSAGE_ID);

    verifyGetCalled(() -> uriPathEndsWithMatcher(MESSAGE_ID), ConversationMessage.class);
  }

  @Test
  void listMessages() {
    final String contactId = "contact-id";
    messages.list(new ListMessagesParams().contactId(contactId));

    verifyGetCalled(() -> uriQueryEndsWithMatcher(contactId), ListMessagesResponse.class);
  }

  @Test
  void sendSimpleMessage() {
    messages.send(new TextMessageRequest("Hej").appId(APP_ID));

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(Messages.SEND),
        SendMessageResponse.class,
        () ->
            argThat(
                (SendMessageRequest req) ->
                    APP_ID.equals(req.getAppId()) && PROJECT_ID.equals(req.getProjectId())));
  }

  @Test
  void sendSimpleMessageAsync() {
    messages
        .sendAsync(new TextMessageRequest("Hej").appId(APP_ID))
        .whenComplete(
            (ign, ign2) ->
                verifyPostCalled(
                    () -> uriPathEndsWithMatcher(Messages.SEND),
                    SendMessageResponse.class,
                    () ->
                        argThat(
                            (SendMessageRequest req) ->
                                APP_ID.equals(req.getAppId())
                                    && PROJECT_ID.equals(req.getProjectId()))));
  }

  @Test
  void sendMessage() {
    messages.send(new SendMessageRequest().appId(APP_ID));

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(Messages.SEND),
        SendMessageResponse.class,
        () ->
            argThat(
                (SendMessageRequest req) ->
                    APP_ID.equals(req.getAppId()) && PROJECT_ID.equals(req.getProjectId())));
  }

  @Test
  void sendMessageDefaultAppId() {
    final String defaultAppId = "defaultAppId";
    getMessages(defaultAppId).send(new SendMessageRequest());

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(Messages.SEND),
        SendMessageResponse.class,
        () ->
            argThat(
                (SendMessageRequest req) ->
                    defaultAppId.equals(req.getAppId()) && PROJECT_ID.equals(req.getProjectId())));
  }

  @Test
  void transcodeMessage() {
    messages.transcode(
        new TranscodeMessageRequest().appId(APP_ID).addChannelsItem(ConversationChannel.SMS));

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(Messages.TRANSCODE),
        TranscodeMessageResponse.class,
        () ->
            argThat(
                (TranscodeMessageRequest req) ->
                    APP_ID.equals(req.getAppId()) && PROJECT_ID.equals(req.getProjectId())));
  }

  @Test
  void transcodeMessageDefaultAppId() {
    final String defaultAppId = "defaultAppId";
    getMessages(defaultAppId)
        .transcode(new TranscodeMessageRequest().addChannelsItem(ConversationChannel.SMS));

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(Messages.TRANSCODE),
        TranscodeMessageResponse.class,
        () ->
            argThat(
                (TranscodeMessageRequest req) ->
                    defaultAppId.equals(req.getAppId()) && PROJECT_ID.equals(req.getProjectId())));
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(final ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowingCallable> callsWithMissingParams() {
    return Arrays.asList(
        () -> messages.get(null),
        () -> messages.list(null),
        () -> messages.list(new ListMessagesParams()),
        () -> messages.delete(null),
        () -> messages.send((SendMessageRequest) null),
        () -> messages.send(new SendMessageRequest()),
        () -> messages.transcode(null),
        () -> messages.transcode(new TranscodeMessageRequest().appId(APP_ID)),
        () -> messages.transcode(new TranscodeMessageRequest().channels(emptyList())));
  }

  private Messages getMessages(final String defaultAppId) {
    return new Messages(PROJECT_ID, restClient, BASE_URL, defaultAppId);
  }
}
