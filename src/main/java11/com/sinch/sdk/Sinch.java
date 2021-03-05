package com.sinch.sdk;

import com.sinch.sdk.api.conversationapi.ConversationApi;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.restclient.JavaRestClientFactory;
import java.net.http.HttpClient;
import lombok.NonNull;

public class Sinch extends SDKInitializer {

  public static ConversationApi conversationApi(@NonNull final Region region) {
    return conversationApi(region, () -> new JavaRestClientFactory(HttpClient.newHttpClient()));
  }
}
