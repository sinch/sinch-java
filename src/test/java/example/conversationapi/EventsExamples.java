package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.factory.RecipientFactory;
import com.sinch.sdk.api.conversationapi.service.Events;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.AppEvent;
import com.sinch.sdk.model.conversationapi.SendEventRequest;
import com.sinch.sdk.model.conversationapi.SendEventResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class EventsExamples {

  private static Events events;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    events = Sinch.conversationApi(Region.EU).events();
  }

  @Test
  void sendEvent() {
    final Map<String, String> composingEvent = new HashMap<>();
    composingEvent.put("", "");
    final SendEventResponse response =
        events.send(
            new SendEventRequest()
                .appId(AppsExamples.APP_ID)
                .event(new AppEvent().composingEvent(composingEvent))
                .recipient(RecipientFactory.fromContactId(ContactsExamples.CONTACT_ID)));
    log.info("{}", response);
  }
}
