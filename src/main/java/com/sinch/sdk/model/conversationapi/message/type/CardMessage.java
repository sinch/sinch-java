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

/* Card Message
 *
 * Message containing text, media and choices. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardMessage {
  // Required.
  private String title;

  // Optional.
  private String description;

  // Optional.
  @JsonProperty("media_message")
  private MediaMessage mediaMessage;

  /* Optional. The number of choices is limited to 3. */
  @Size(max = 3)
  private List<Choice> choices;
}
