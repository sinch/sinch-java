package com.sinch.sdk.api.conversationapi;

import lombok.Data;

// TODO: smart solution for configuration
@Data
public class ConversationApiConfig {
  private String baseUrl;
  private String version;
  private String projectId;
}
