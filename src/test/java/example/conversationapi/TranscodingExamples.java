package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.factory.MessageFactory;
import com.sinch.sdk.api.conversationapi.service.Transcoding;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.AppMessage;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.TranscodeMessageRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class TranscodingExamples {

  private static Transcoding transcoding;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    transcoding = Sinch.conversationApi(Region.EU).transcoding();
  }

  @Test
  void transcodeMessage() {
    final Map<String, String> response =
        transcoding.transcodeMessage(
            new TranscodeMessageRequest()
                .appMessage(
                    new AppMessage().textMessage(MessageFactory.textMessage("SDK text message")))
                .addChannelsItem(ConversationChannel.MESSENGER)
                .addChannelsItem(ConversationChannel.RCS)
                .appId(AppsExamples.APP_ID));
    log.info("{}", response);
  }
}
