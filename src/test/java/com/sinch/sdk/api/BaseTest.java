package com.sinch.sdk.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sinch.sdk.api.conversationapi.ConversationApi;
import java.lang.reflect.Method;
import lombok.SneakyThrows;

public class BaseTest {
  protected static final ObjectMapper OM = objectMapper();
  private static final ObjectWriter OW = OM.writerWithDefaultPrettyPrinter();

  @SneakyThrows
  protected void prettyPrint(final Object value) {
    System.out.println(OW.writeValueAsString(value));
  }

  @SneakyThrows
  private static ObjectMapper objectMapper() {
    final Method method = ConversationApi.class.getDeclaredMethod("objectMapper");
    method.setAccessible(true);
    return (ObjectMapper) method.invoke(null);
  }
}
