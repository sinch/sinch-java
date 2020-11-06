package com.sinch.sdk.model.conversationapi.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.message.type.ChoiceResponseMessage;
import com.sinch.sdk.model.conversationapi.message.type.LocationMessage;
import com.sinch.sdk.model.conversationapi.message.type.MediaMessage;
import com.sinch.sdk.model.conversationapi.message.type.TextMessage;
import com.sinch.sdk.model.conversationapi.message.type.common.ReplyTo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Message originating from a contact
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class ContactMessage {

  @JsonProperty("text_message")
  private TextMessage textMessage;
  @JsonProperty("media_message")
  private MediaMessage mediaMessage;
  @JsonProperty("location_message")
  private LocationMessage locationMessage;
  @JsonProperty("choice_response_message")
  private ChoiceResponseMessage choiceResponseMessage;
  // Optional. Included if the contact message is a response to a previous App message.
  @JsonProperty("reply_to")
  private ReplyTo replyTo;

  @Builder(builderMethodName = "fromTextMessage", builderClassName = "TextContactMessage")
  public ContactMessage(TextMessage textMessage, ReplyTo replyTo) {
    this.textMessage = textMessage;
    this.replyTo = replyTo;
  }

  @Builder(builderMethodName = "fromMediaMessage", builderClassName = "MediaContactMessage")
  public ContactMessage(MediaMessage mediaMessage, ReplyTo replyTo) {
    this.mediaMessage = mediaMessage;
    this.replyTo = replyTo;
  }

  @Builder(builderMethodName = "fromLocationMessage", builderClassName = "LocationContactMessage")
  public ContactMessage(LocationMessage locationMessage, ReplyTo replyTo) {
    this.locationMessage = locationMessage;
    this.replyTo = replyTo;
  }

  @Builder(
      builderMethodName = "fromChoiceResponseMessage",
      builderClassName = "ChoiceResponseContactMessage")
  public ContactMessage(ChoiceResponseMessage choiceResponseMessage, ReplyTo replyTo) {
    this.choiceResponseMessage = choiceResponseMessage;
    this.replyTo = replyTo;
  }
}
