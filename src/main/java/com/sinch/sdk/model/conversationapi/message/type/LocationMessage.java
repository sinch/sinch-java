package com.sinch.sdk.model.conversationapi.message.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Message with geo location
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationMessage {
  /* Required. The title is shown close to the
   * button or link that leads to a map showing the location.
   * The title is clickable in some cases. */
  private String title;

  // Required. Geo coordinates.
  private Coordinates coordinates;

  // Optional. Label or name for the position.
  private String label;

  // Geographic coordinates
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Coordinates {
    // Required. The latitude.
    private float latitude;

    // Required. The longitude.
    private float longitude;
  }
}
