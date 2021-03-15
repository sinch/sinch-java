package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.TranscodeMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TranscodingTest extends BaseServiceTest {

  private static Transcoding transcoding;

  @BeforeEach
  void setUp() {
    transcoding = new Transcoding(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final Transcoding transcoding = new Transcoding(CONFIG, null);
    assertThat(transcoding.restClient).isNotNull();
    assertThat(transcoding.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, transcoding.getServiceName()));
  }

  @Test
  void transcodeMessage() {
    // For coverage, asserted in MessageTest
    final String appId = "app-id";
    transcoding.transcodeMessage(
        new TranscodeMessageRequest().addChannelsItem(ConversationChannel.RCS).appId(appId));
    transcoding.transcodeMessageAsync(
        new TranscodeMessageRequest().addChannelsItem(ConversationChannel.RCS).appId(appId));
  }
}
