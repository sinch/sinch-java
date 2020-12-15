package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.model.common.Region;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ConversationApiConfig {
  private final String projectId;
  @Builder.Default private final String version = "v1";
  private final Region region;
  private final SinchRestClient restClient;
}
