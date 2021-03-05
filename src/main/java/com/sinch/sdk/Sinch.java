package com.sinch.sdk;

import com.sinch.sdk.api.conversationapi.ConversationApi;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.restclient.OkHttpRestClientFactory;
import lombok.NonNull;
import okhttp3.OkHttpClient;

public class Sinch extends SDKInitializer {

  public static ConversationApi conversationApi(@NonNull final Region region) {
    return conversationApi(region, () -> new OkHttpRestClientFactory(new OkHttpClient()));
  }
}
