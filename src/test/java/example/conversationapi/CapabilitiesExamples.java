package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.factory.RecipientFactory;
import com.sinch.sdk.api.conversationapi.service.Capabilities;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.QueryCapabilityResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class CapabilitiesExamples {

  private static Capabilities capabilities;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    capabilities = Sinch.conversationApi(Region.EU).capabilities();
  }

  @Test
  void queryCapability() {
    final QueryCapabilityResponse response =
        capabilities.query(
            new QueryCapabilityRequest()
                .appId(AppsExamples.APP_ID)
                .recipient(RecipientFactory.fromContactId(ContactsExamples.CONTACT_ID)));
    log.info("{}", response);
  }
}
