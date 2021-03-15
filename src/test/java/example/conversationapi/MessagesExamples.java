package example.conversationapi;

import static com.sinch.sdk.api.conversationapi.factory.ChoiceFactory.choice;
import static com.sinch.sdk.api.conversationapi.factory.MessageFactory.callMessage;
import static com.sinch.sdk.api.conversationapi.factory.MessageFactory.cardMessage;
import static com.sinch.sdk.api.conversationapi.factory.MessageFactory.locationMessage;
import static com.sinch.sdk.api.conversationapi.factory.MessageFactory.mediaMessage;
import static com.sinch.sdk.api.conversationapi.factory.MessageFactory.textMessage;
import static com.sinch.sdk.api.conversationapi.factory.MessageFactory.urlMessage;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.factory.RecipientFactory;
import com.sinch.sdk.api.conversationapi.model.ListMessagesParams;
import com.sinch.sdk.api.conversationapi.model.request.message.CardMessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.CarouselMessageRequest;
import com.sinch.sdk.api.conversationapi.service.Messages;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.AppMessage;
import com.sinch.sdk.model.conversationapi.CallMessage;
import com.sinch.sdk.model.conversationapi.CardMessage;
import com.sinch.sdk.model.conversationapi.CarouselMessage;
import com.sinch.sdk.model.conversationapi.Choice;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.ConversationMessage;
import com.sinch.sdk.model.conversationapi.Coordinates;
import com.sinch.sdk.model.conversationapi.ListMessagesResponse;
import com.sinch.sdk.model.conversationapi.LocationMessage;
import com.sinch.sdk.model.conversationapi.MediaMessage;
import com.sinch.sdk.model.conversationapi.SendMessageRequest;
import com.sinch.sdk.model.conversationapi.SendMessageResponse;
import com.sinch.sdk.model.conversationapi.TextMessage;
import com.sinch.sdk.model.conversationapi.TranscodeMessageRequest;
import com.sinch.sdk.model.conversationapi.UrlMessage;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class MessagesExamples {

  private static final String MESSAGE_ID = "message-id";

  private static Messages messages;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    messages = Sinch.conversationApi(Region.EU).messages(AppsExamples.APP_ID);
  }

  @Test
  void deleteMessage() {
    messages.delete(MESSAGE_ID);
  }

  @Test
  void getMessage() {
    final ConversationMessage response = messages.get(MESSAGE_ID);
    log.info("{}", response);
  }

  @Test
  void listMessages() {
    final ListMessagesResponse response =
        messages.list(new ListMessagesParams().contactId(ContactsExamples.CONTACT_ID));
    log.info("{}", response);
  }

  @Test
  void sendMessage() {
    final SendMessageResponse response =
        messages.send(
            new SendMessageRequest()
                .message(
                    new AppMessage()
                        .carouselMessage(
                            new CarouselMessage()
                                .addCardsItem(
                                    new CardMessage()
                                        .title("This is the card 1 title")
                                        .description("This is the card 1 description")
                                        .mediaMessage(
                                            new MediaMessage()
                                                .url(
                                                    "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png"))
                                        .addChoicesItem(
                                            new Choice()
                                                .textMessage(
                                                    new TextMessage()
                                                        .text("Suggested Reply 1 Text")))
                                        .addChoicesItem(
                                            new Choice()
                                                .textMessage(
                                                    new TextMessage()
                                                        .text("Suggested Reply 2 Text")))
                                        .addChoicesItem(
                                            new Choice()
                                                .textMessage(
                                                    new TextMessage()
                                                        .text("Suggested Reply 3 Text"))))
                                .addCardsItem(
                                    new CardMessage()
                                        .title("This is the card 2 title")
                                        .description("This is the card 2 description")
                                        .mediaMessage(
                                            new MediaMessage()
                                                .url(
                                                    "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png"))
                                        .addChoicesItem(
                                            new Choice()
                                                .urlMessage(
                                                    new UrlMessage()
                                                        .title("URL Choice Message:")
                                                        .url("https://www.sinch.com"))))
                                .addCardsItem(
                                    new CardMessage()
                                        .title("This is the card 3 title")
                                        .description("This is the card 3 description")
                                        .mediaMessage(
                                            new MediaMessage()
                                                .url(
                                                    "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png"))
                                        .addChoicesItem(
                                            new Choice()
                                                .callMessage(
                                                    new CallMessage()
                                                        .title("Call Choice Message:")
                                                        .phoneNumber("46732000000"))))
                                .addCardsItem(
                                    new CardMessage()
                                        .title("This is the card 4 title")
                                        .description("This is the card 4 description")
                                        .mediaMessage(
                                            new MediaMessage()
                                                .url(
                                                    "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png"))
                                        .addChoicesItem(
                                            new Choice()
                                                .locationMessage(
                                                    new LocationMessage()
                                                        .title("Location Choice Message")
                                                        .label("Enriching Engagement")
                                                        .coordinates(
                                                            new Coordinates()
                                                                .latitude(55.610479f)
                                                                .longitude(13.002873f)))))))
                .recipient(RecipientFactory.fromContactId(ContactsExamples.CONTACT_ID))
                .addChannelPriorityOrderItem(ConversationChannel.SMS));
    log.info("{}", response);
  }

  /**
   * This message is identical to the message above except for the channel priority, to add that
   * simply convert it to the raw request by calling '.getRequest()' and add it before sending.
   */
  @Test
  void sendSimpleMessage() {
    final SendMessageResponse response =
        messages.send(
            new CarouselMessageRequest()
                .addCard( // Here I use the CardMessageRequest as a builder for the card message
                    new CardMessageRequest("This is the card 1 title")
                        .description("This is the card 1 description")
                        .media(
                            "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png")
                        .addTextChoice("Suggested Reply 1 Text")
                        .addTextChoice("Suggested Reply 2 Text")
                        .addTextChoice("Suggested Reply 3 Text")
                        .getMessage())
                .addCard( // Here I use the factory methods directly
                    cardMessage("This is the card 2 title")
                        .description("This is the card 2 description")
                        .mediaMessage(
                            mediaMessage(
                                "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png"))
                        .addChoicesItem(
                            choice(urlMessage("URL Choice Message:", "https://www.sinch.com"))))
                .addCard(
                    cardMessage("This is the card 3 title")
                        .description("This is the card 3 description")
                        .mediaMessage(
                            mediaMessage(
                                "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png"))
                        .addChoicesItem(choice(callMessage("Call Choice Message:", "46732000000"))))
                .addCard(
                    cardMessage("This is the card 4 title")
                        .description("This is the card 4 description")
                        .mediaMessage(
                            mediaMessage(
                                "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/Sinch-logo-Events.png"))
                        .addChoicesItem(
                            choice(
                                locationMessage("Location Choice Message", 13.002873f, 55.610479f)
                                    .label("Enriching Engagement"))))
                .contactRecipient(ContactsExamples.CONTACT_ID));
    log.info("{}", response);
  }

  @Test
  void transcodeMessage() {
    final Map<String, String> response =
        messages.transcode(
            new TranscodeMessageRequest()
                .appMessage(new AppMessage().textMessage(textMessage("SDK text message")))
                .addChannelsItem(ConversationChannel.VIBER)
                .addChannelsItem(ConversationChannel.WHATSAPP));
    log.info("{}", response);
  }
}
