package com.sinch.sdk.model.conversationapi.app.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinch.sdk.model.conversationapi.app.App;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListAppsResponse {
  // List of apps belonging to a specific project ID.
  private List<App> apps;
}
