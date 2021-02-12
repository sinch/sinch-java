package com.sinch.sdk.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import java.io.InputStream;
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
  protected <T> T getResource(final String filePath, final TypeReference<T> typeReference) {
    return OM.readValue(getResource(filePath), typeReference);
  }

  protected InputStream getResource(final String filePath) {
    return getClass().getClassLoader().getResourceAsStream(filePath);
  }

  @SneakyThrows
  private static ObjectMapper objectMapper() {
    final Method method = ConversationApiClient.class.getDeclaredMethod("objectMapper");
    method.setAccessible(true);
    return (ObjectMapper) method.invoke(null);
  }
}
