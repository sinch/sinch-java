package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.restclient.SinchRestClient;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ConversationApiConfig {
  private final String projectId;
  private final String baseUrl;
  private final SinchRestClient restClient;
}
