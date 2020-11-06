package com.sinch.sdk.model.conversationapi.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.message.type.CardMessage;
import com.sinch.sdk.model.conversationapi.message.type.CarouselMessage;
import com.sinch.sdk.model.conversationapi.message.type.ChoiceMessage;
import com.sinch.sdk.model.conversationapi.message.type.LocationMessage;
import com.sinch.sdk.model.conversationapi.message.type.MediaMessage;
import com.sinch.sdk.model.conversationapi.message.type.TemplateMessage;
import com.sinch.sdk.model.conversationapi.message.type.TextMessage;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* The content of the message.
 * (One of TextMessage, MediaMessage,
 * TemplateMessage, ChoiceMessage,
 * CardMessage, CarouselMessage or
 * LocationMessage) OR explicitChannelMessage. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class AppMessage {

  /* Optional. Channel specific messages, overriding any transcoding.
   * The key in the map must point to a valid conversation channel as
   * defined by the enum ConversationChannel. */
  @JsonProperty("explicit_channel_message")
  private Map<String, String> explicitChannelMessage;
  // A message containing only text.
  @JsonProperty("text_message")
  private TextMessage textMessage;
  // A message containing media such as images, GIFs, and video.
  @JsonProperty("media_message")
  private MediaMessage mediaMessage;
  /* A message with predefined template.
   * Requires an existing template. */
  @JsonProperty("template_message")
  private TemplateMessage templateMessage;
  /* A message containing a "choice"/"action" and description.
   * A choice message is transcoded as a button in Messenger and RCS
   * and as a bullet point in SMS and WhatsApp. */
  @JsonProperty("choice_message")
  private ChoiceMessage choiceMessage;
  /* A rich message which consists of text and description with image or video.
   * It can also contain a set of "choices" ("actions"). */
  @JsonProperty("card_message")
  private CardMessage cardMessage;
  /* A list of cards rendered horizontally on supported channels (Messenger and RCS)
   * and as a numbered list on SMS and WhatsApp. */
  @JsonProperty("carousel_message")
  private CarouselMessage carouselMessage;
  // A message defining a physical location on a map.
  @JsonProperty("location_message")
  private LocationMessage locationMessage;

  @Builder(builderMethodName = "fromTextMessage", builderClassName = "TextAppMessage")
  public AppMessage(TextMessage textMessage) {
    this.textMessage = textMessage;
  }

  @Builder(builderMethodName = "fromMediaMessage", builderClassName = "MediaAppMessage")
  public AppMessage(MediaMessage mediaMessage) {
    this.mediaMessage = mediaMessage;
  }

  @Builder(builderMethodName = "fromChoiceMessage", builderClassName = "ChoiceAppMessage")
  public AppMessage(ChoiceMessage choiceMessage) {
    this.choiceMessage = choiceMessage;
  }

  @Builder(builderMethodName = "fromCardMessage", builderClassName = "CardAppMessage")
  public AppMessage(CardMessage cardMessage) {
    this.cardMessage = cardMessage;
  }

  @Builder(builderMethodName = "fromCarouselMessage", builderClassName = "CarouselAppMessage")
  public AppMessage(CarouselMessage carouselMessage) {
    this.carouselMessage = carouselMessage;
  }

  @Builder(builderMethodName = "fromLocationMessage", builderClassName = "LocationAppMessage")
  public AppMessage(LocationMessage locationMessage) {
    this.locationMessage = locationMessage;
  }

  @Builder(builderMethodName = "fromTemplateMessage", builderClassName = "TemplateAppMessage")
  public AppMessage(TemplateMessage templateMessage) {
    this.templateMessage = templateMessage;
  }

  @Builder(builderMethodName = "fromExplicitMessage", builderClassName = "ExplicitAppMessage")
  public AppMessage(Map<String, String> explicitChannelMessage) {
    this.explicitChannelMessage = explicitChannelMessage;
  }
}
