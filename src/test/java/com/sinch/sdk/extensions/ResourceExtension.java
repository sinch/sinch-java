package com.sinch.sdk.extensions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sinch.sdk.api.conversationapi.ConversationApi;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ResourceExtension implements ParameterResolver {

  private static final ObjectMapper MAPPER;
  private static final ObjectWriter WRITER;

  static {
    MAPPER = objectMapper();
    WRITER = MAPPER.writerWithDefaultPrettyPrinter();
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(Resource.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    final Resource paramDefinition = parameterContext.getParameter().getAnnotation(Resource.class);
    if (String.class.equals(paramDefinition.type())) {
      return getResourceAsString(paramDefinition.value());
    } else if (InputStream.class.equals(paramDefinition.type())) {
      return getResourceAsInputStream(paramDefinition.value());
    } else {
      return getResource(
          paramDefinition.value(),
          new TypeReference<Type>() {
            @Override
            public Type getType() {
              return paramDefinition.type();
            }
          });
    }
  }

  @SneakyThrows
  private <T> T getResource(final String filePath, final TypeReference<T> typeReference) {
    return MAPPER.readValue(getResourceAsInputStream(filePath), typeReference);
  }

  private InputStream getResourceAsInputStream(final String filePath) {
    return getClass().getClassLoader().getResourceAsStream(filePath);
  }

  private String getResourceAsString(String resourceName) {
    return new BufferedReader(
            new InputStreamReader(getResourceAsInputStream(resourceName), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  @SneakyThrows
  private static ObjectMapper objectMapper() {
    final Method method = ConversationApi.class.getDeclaredMethod("objectMapper");
    method.setAccessible(true);
    return (ObjectMapper) method.invoke(null);
  }

  @SneakyThrows
  public static String asJsonString(final Object value) {
    return WRITER.writeValueAsString(value);
  }

  @Target({ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Resource {

    /** Path of the resource to load */
    String value() default "";

    /** Optional type of the resource */
    Class<?> type() default String.class;
  }
}
