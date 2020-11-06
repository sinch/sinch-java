package com.sinch.sdk.model.conversationapi.message.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sinch.sdk.model.conversationapi.message.type.common.Choice;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Choice Message
 *
 * Message containing choices/actions. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceMessage {
  // Required.
  @JsonProperty("text_message")
  private TextMessage textMessage;

  // Required. The number of choices is limited to 3.
  @Size(max = 3)
  private List<Choice> choices;
}
