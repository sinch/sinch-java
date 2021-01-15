package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.TypeAppMessage;
import com.sinch.sdk.model.conversationapi.TypeConversationChannel;
import com.sinch.sdk.model.conversationapi.TypeTextMessage;
import com.sinch.sdk.model.conversationapi.V1TranscodeMessageRequest;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TranscodingServiceTest extends BaseConvIntegrationTest {

  private static TranscodingService transcodingService;

  private final String appId = "your-app-id";

  @BeforeAll
  static void beforeAll() {
    transcodingService = Sinch.conversationApi(Region.EU).transcoding();
  }

  @Test
  void testTranscodeMessage() throws ApiException {
    final Map<String, String> response =
        transcodingService.transcodeMessage(
            new V1TranscodeMessageRequest()
                .appMessage(
                    new TypeAppMessage()
                        .textMessage(new TypeTextMessage().text("SDK text message")))
                .addChannelsItem(TypeConversationChannel.MESSENGER)
                .addChannelsItem(TypeConversationChannel.RCS)
                .appId(appId));
    prettyPrint(response);
  }
}
