package com.sinch.sdk;

import static com.sinch.sdk.utils.StringUtils.isEmpty;

import com.sinch.sdk.api.conversationapi.ConversationApi;
import com.sinch.sdk.configuration.ExternalConfiguration;
import com.sinch.sdk.exception.ConfigurationException;
import com.sinch.sdk.model.common.Region;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Sinch {

  private Config sinchConfig;
  private ConversationApi conversationApi;

  static {
    final String keyId = ExternalConfiguration.getKeyId();
    final String keySecret = ExternalConfiguration.getKeySecret();
    final String projectId = ExternalConfiguration.getProjectId();
    if (!(isEmpty(keyId) || isEmpty(keySecret) || isEmpty(projectId))) {
      init(keyId, keySecret, projectId);
    }
  }

  /**
   * Initialize the Sinch environment.
   *
   * @param keyId account to use
   * @param keySecret auth token for the account
   * @param projectId the project id
   */
  public void init(
      @NonNull final String keyId,
      @NonNull final String keySecret,
      @NonNull final String projectId) {
    invalidate();
    sinchConfig = Config.builder().keyId(keyId).keySecret(keySecret).projectId(projectId).build();
  }

  public ConversationApi conversationApi(@NonNull final Region region) {
    validate();
    return Optional.ofNullable(conversationApi)
        .filter(client -> client.region() == region)
        .orElseGet(
            () -> {
              conversationApi = new ConversationApi(region, sinchConfig);
              return conversationApi;
            });
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
    private final String keyId;
    private final String keySecret;
    private final String projectId;
  }
}
