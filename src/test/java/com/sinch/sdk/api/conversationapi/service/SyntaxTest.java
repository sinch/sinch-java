package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.factory.ChoiceFactory;
import com.sinch.sdk.api.conversationapi.factory.MessageFactory;
import com.sinch.sdk.api.conversationapi.model.request.message.CardMessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.CarouselMessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.ChoiceMessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.LocationMessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.MediaMessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.MessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.TemplateMessageRequest;
import com.sinch.sdk.api.conversationapi.model.request.message.TextMessageRequest;
import com.sinch.sdk.model.conversationapi.CardHeight;
import com.sinch.sdk.model.conversationapi.ContactLanguage;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.SendMessageRequest;
import com.sinch.sdk.model.conversationapi.TemplateReference;
import com.sinch.sdk.test.extension.ResourceExtension;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResourceExtension.class)
public class SyntaxTest {

  private static final String RESOURCE_PATH = "conversation-api/requests/";

  @Test
  void testMessage(
      @ResourceExtension.Resource(RESOURCE_PATH + "text-message.json") final String expected) {
    assertThat(new TextMessageRequest("Hej").smsRecipient("123")).isEqualTo(expected);
  }

  @Test
  void mediaMessage(
      @ResourceExtension.Resource(RESOURCE_PATH + "media-message.json") final String expected) {
    assertThat(
            new MediaMessageRequest("https://media.url")
                .thumbnail("https://media.thumbnail.url")
                .rcsRecipient("+123445566"))
        .isEqualTo(expected);
  }

  @Test
  void choiceMessage(
      @ResourceExtension.Resource(RESOURCE_PATH + "choice-message.json") final String expected) {
    assertThat(
            new ChoiceMessageRequest("Choose")
                .appId("123")
                .addLocationChoice("Meet here", 44.222f, 66.333f)
                .addTextChoice("Message")
                .addUrlChoice("Homepage", "www.google.com")
                .whatsappRecipient("123"))
        .isEqualTo(expected);
  }

  @Test
  void cardMessage(
      @ResourceExtension.Resource(RESOURCE_PATH + "card-message.json") final String expected) {
    assertThat(cardMessageRequest()).isEqualTo(expected);
  }

  @Test
  void carouselMessage(
      @ResourceExtension.Resource(RESOURCE_PATH + "carousel-message.json") final String expected) {
    assertThat(
            new CarouselMessageRequest()
                .addCallChoice("My number", "+321665544")
                .addCard(cardMessageRequest().getMessage())
                .addCard(
                    MessageFactory.cardMessage("Other Title")
                        .description("Descriptioner")
                        .mediaMessage(
                            MessageFactory.mediaMessage("file://img")
                                .thumbnailUrl("file://small_img"))
                        .addChoicesItem(
                            ChoiceFactory.choice(MessageFactory.callMessage("a", "numbah"))))
                .messengerRecipient("msngrrr"))
        .isEqualTo(expected);
  }

  @Test
  void locationMessage(
      @ResourceExtension.Resource(RESOURCE_PATH + "location-message.json") final String expected) {
    assertThat(
            new LocationMessageRequest("Here!", 22.445f, 55.512f)
                .label("The location label")
                .viberBMRecipient("bmviberer"))
        .isEqualTo(expected);
  }

  @Test
  void templateMessage(
      @ResourceExtension.Resource(RESOURCE_PATH + "template-message.json") final String expected) {
    assertThat(
            new TemplateMessageRequest()
                .channelTemplateItem(
                    ConversationChannel.WHATSAPP,
                    new TemplateReference()
                        .templateId("tempateId")
                        .languageCode(ContactLanguage.SV.getValue())
                        .putParametersItem("header[1]image.link", "https://example.com"))
                .omniTemplate(
                    new TemplateReference()
                        .templateId("omniId")
                        .languageCode(ContactLanguage.EN.getValue())
                        .version("v5")
                        .putParametersItem("name", "Ms Smith"))
                .contactRecipient("adf123")
                .getRequest()
                .addChannelPriorityOrderItem(ConversationChannel.WHATSAPP)
                .addChannelPriorityOrderItem(ConversationChannel.SMS))
        .isEqualTo(expected);
  }

  private CardMessageRequest cardMessageRequest() {
    return new CardMessageRequest("Card Title")
        .appId("123")
        .description("Card Description")
        .media(
            "https://1vxc0v12qhrm1e72gq1mmxkf-wpengine.netdna-ssl.com/wp-content/uploads/2020/07/Sinch-Voice-calling-graphics.png")
        .addCallChoice("Call me!", "+123445566")
        .addUrlChoice("Join my meeting!", "https://slack.meeting.com")
        .height(CardHeight.SHORT)
        .viberRecipient("vibrrrr");
  }

  private AbstractStringAssert<?> assertThat(final MessageRequest<?, ?> request) {
    return assertThat(request.getRequest());
  }

  private AbstractStringAssert<?> assertThat(final SendMessageRequest request) {
    return Assertions.assertThat(ResourceExtension.asJsonString(request));
  }
}
