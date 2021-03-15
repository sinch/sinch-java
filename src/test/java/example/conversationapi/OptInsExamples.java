package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.factory.RecipientFactory;
import com.sinch.sdk.api.conversationapi.service.OptIns;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.OptIn;
import com.sinch.sdk.model.conversationapi.OptInResponse;
import com.sinch.sdk.model.conversationapi.OptOut;
import com.sinch.sdk.model.conversationapi.OptOutResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class OptInsExamples {

  private static OptIns optIns;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    optIns = Sinch.conversationApi(Region.EU).optIns();
  }

  @Test
  void registerOptIn() {
    final OptInResponse response =
        optIns.optIn(
            new OptIn()
                .appId(AppsExamples.APP_ID)
                .addChannelsItem(ConversationChannel.WHATSAPP)
                .recipient(RecipientFactory.fromContactId(ContactsExamples.CONTACT_ID)),
            null);
    log.info("{}", response);
  }

  @Test
  void registerOptOut() {
    final OptOutResponse response =
        optIns.optOut(
            new OptOut()
                .appId(AppsExamples.APP_ID)
                .addChannelsItem(ConversationChannel.WHATSAPP)
                .recipient(RecipientFactory.fromContactId(ContactsExamples.CONTACT_ID)),
            null);
    log.info("{}", response);
  }
}
