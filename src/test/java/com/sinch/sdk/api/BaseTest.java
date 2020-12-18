package com.sinch.sdk.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sinch.sdk.ObjectMappers;
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
  protected <T> T getResource(final String filePath, final Class<T> clazz) {
    return OM.readValue(getResource(filePath), clazz);
  }

  protected InputStream getResource(final String filePath) {
    return getClass().getClassLoader().getResourceAsStream(filePath);
  }

  @SneakyThrows
  private static ObjectMapper objectMapper() {
    final Method method = ObjectMappers.class.getDeclaredMethod("conversationApiMapper");
    method.setAccessible(true);
    return (ObjectMapper) method.invoke(null);
  }
}
