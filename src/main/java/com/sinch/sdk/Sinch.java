package com.sinch.sdk;

import static com.sinch.sdk.utils.StringUtils.isEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    sinchConfig =
        Config.builder().clientId(clientId).clientSecret(clientSecret).projectId(projectId).build();
  }

  public ConversationApiClient conversationApi() {
    validate();
    return Optional.ofNullable(conversationApi)
        .orElseGet(
            () -> {
              final HttpClient httpClient = HttpClient.newHttpClient();
              final ObjectMapper objectMapper = ObjectMappers.conversationApiMapper();
              final AuthenticationService authenticationService =
                  authenticationService(httpClient, objectMapper);
              conversationApi =
                  new ConversationApiClient(
                      ConversationApiConfig.builder()
                          .projectId(sinchConfig.projectId)
                          .restClient(
                              new SinchRestClient(authenticationService, httpClient, objectMapper))
                          .build());
              return conversationApi;
            });
  }

  private AuthenticationService authenticationService(
      final HttpClient httpClient, final ObjectMapper objectMapper) {
    return new AuthenticationService(
        httpClient,
        objectMapper,
        TimeUnit.MINUTES.toSeconds(5),
        sinchConfig.clientId,
        sinchConfig.clientSecret);
  }

  @SneakyThrows
  private void validate() {
    Optional.ofNullable(sinchConfig)
        .orElseThrow(
            () ->
                new ConfigurationException(
                    "Sinch is not initiated, configure client using .init(...)"));
  }

  private void invalidate() {
    conversationApi = null;
  }

  @Getter
  @Builder
  public static final class Config {
    private final String clientId;
    private final String clientSecret;
    private final String projectId;
  }
}
