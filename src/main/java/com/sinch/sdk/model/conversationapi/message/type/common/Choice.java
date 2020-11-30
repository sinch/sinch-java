package com.sinch.sdk.model.conversationapi.message.type.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.message.type.LocationMessage;
import com.sinch.sdk.model.conversationapi.message.type.TextMessage;
import lombok.Builder;
import lombok.Data;

/* Choice/Action
 *
 * A choice is an action the user can take such as
 * buttons for quick replies, call actions etc. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Choice {

  // The choice content.
  @JsonProperty("text_message")
  private TextMessage textMessage;
  // The choice content.
  @JsonProperty("url_message")
  private UrlMessage urlMessage;
  // The choice content.
  @JsonProperty("call_message")
  private CallMessage callMessage;
  // The choice content.
  @JsonProperty("location_message")
  private LocationMessage locationMessage;
  /* Optional. This data will be returned in the ChoiceResponseMessage.
   * The default is message_id_{text, title}. */
  @JsonProperty("postback_data")
  private String postbackData;

  @Builder(builderMethodName = "fromTextMessage", builderClassName = "TextChoice")
  public Choice(TextMessage textMessage, String postbackData) {
    this.textMessage = textMessage;
    this.postbackData = postbackData;
  }

  @Builder(builderMethodName = "fromUrlMessage", builderClassName = "UrlChoice")
  public Choice(UrlMessage urlMessage, String postbackData) {
    this.urlMessage = urlMessage;
    this.postbackData = postbackData;
  }

  @Builder(builderMethodName = "fromCallMessage", builderClassName = "CallChoice")
  public Choice(CallMessage callMessage, String postbackData) {
    this.callMessage = callMessage;
    this.postbackData = postbackData;
  }

  @Builder(builderMethodName = "fromLocationMessage", builderClassName = "LocationChoice")
  public Choice(LocationMessage locationMessage, String postbackData) {
    this.locationMessage = locationMessage;
    this.postbackData = postbackData;
  }
}
