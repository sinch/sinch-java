package com.sinch.sdk;

import static com.sinch.sdk.utils.StringUtils.isEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import java.net.http.HttpClient;
import java.util.Optional;
import javax.naming.ConfigurationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Sinch {

  private Config sinchConfig;
  private ConversationApiClient conversationApi;

  static {
    final String clientId = System.getProperty("sinch.id");
    final String clientSecret = System.getProperty("sinch.secret");
    final String projectId = System.getProperty("sinch.project_id");
    if (!(isEmpty(clientId) || isEmpty(clientSecret) || isEmpty(projectId))) {
      init(clientId, clientSecret, projectId);
    }
  }

  /**
   * Initialize the Sinch environment.
   *
   * @param clientId account to use
   * @param clientSecret auth token for the account
   * @param projectId the project id
   */
  public void init(
      @NonNull final String clientId,
      @NonNull final String clientSecret,
      @NonNull final String projectId) {
    invalidate();
    final ObjectMapper objectMapper =
        new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    final HttpClient httpClient = HttpClient.newHttpClient();
    final AuthenticationService authenticationService =
        new AuthenticationService(httpClient, objectMapper, clientId, clientSecret);
    sinchConfig =
        Config.builder()
            .projectId(projectId)
            .restClient(new SinchRestClient(authenticationService, httpClient, objectMapper))
            .build();
  }

  public ConversationApiClient conversationApi() {
    validate();
    return Optional.ofNullable(conversationApi)
        .orElseGet(
            () -> {
              conversationApi = new ConversationApiClient(sinchConfig);
              return conversationApi;
            });
  }

  @SneakyThrows
  private void validate() {
    Optional.ofNullable(sinchConfig)
        .orElseThrow(
            () -> new ConfigurationException("Sinch is not initiated, initiate using .init(...)"));
  }

  private void invalidate() {
    sinchConfig = null;
    conversationApi = null;
  }

  @Getter
  @Builder
  public static final class Config {
    private final String projectId;
    private final SinchRestClient restClient;
  }
}
