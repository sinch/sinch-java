package com.sinch.sdk.model.conversationapi.message.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinch.sdk.model.conversationapi.message.type.common.Choice;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Carousel Message
 *
 * Message containing a list of cards often
 * rendered horizontally on supported channels. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarouselMessage {
  // Required. A list of 1..10 cards.
  @Size(max = 10, min = 1)
  private List<CardMessage> cards;

  /* Optional. Outer choices on the carousel level.
   * The number of outer choices is limited to 3. */
  @Size(max = 3)
  private List<Choice> choices;
}
