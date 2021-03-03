package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.AppMessage;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.TextMessage;
import com.sinch.sdk.model.conversationapi.TranscodeMessageRequest;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TranscodingTest extends BaseConvIntegrationTest {

  private static Transcoding transcoding;

  private final String appId = "your-app-id";

  @BeforeAll
  static void beforeAll() {
    transcoding =
        Sinch.conversationApi(Region.EU, () -> new OkHttpRestClientFactory(new OkHttpClient()))
            .transcoding();
  }

  @Test
  void testTranscodeMessage() {
    final Map<String, String> response =
        transcoding.transcodeMessage(
            new TranscodeMessageRequest()
                .appMessage(
                    new AppMessage().textMessage(new TextMessage().text("SDK text message")))
                .addChannelsItem(ConversationChannel.MESSENGER)
                .addChannelsItem(ConversationChannel.RCS)
                .appId(appId));
    prettyPrint(response);
  }
}
