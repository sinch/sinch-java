package com.sinch.sdk.client;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import java.util.Optional;

// TODO: The future of this sdk is that it should include all Sinch services - so maybe this client
// class can manage them.
public class SinchClient {
  private ConversationApiClient conversationApi;

  public ConversationApiClient conversationApi() {
    return Optional.of(conversationApi)
        .orElseGet(
            () -> {
              conversationApi = new ConversationApiClient();
              return conversationApi;
            });
  }
}
