package com.sinch.sdk.api.conversationapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.service.*;
import com.sinch.sdk.configuration.Configuration;
import com.sinch.sdk.model.common.Region;
import java.net.http.HttpClient;
import lombok.NonNull;

public class ConversationApiClient {

  private final Region region;
  private ConversationApiConfig config;

  public ConversationApiClient(final Region region, final Sinch.Config sinchConfig) {
    final HttpClient httpClient = HttpClient.newHttpClient();
    final ObjectMapper objectMapper = objectMapper();
    final Configuration regionConfig = Configuration.forRegion(region);
    final AuthenticationService authenticationService =
        new AuthenticationService(
            httpClient,
            objectMapper,
            regionConfig.authentication(),
            sinchConfig.getKeyId(),
            sinchConfig.getKeySecret());

    this.region = region;
    this.config =
        ConversationApiConfig.builder()
            .projectId(sinchConfig.getProjectId())
            .baseUrl(regionConfig.conversationApi().getUrl())
            .restClient(new SinchRestClient(authenticationService, httpClient, objectMapper))
            .build();
  }

  public Region region() {
    return region;
  }

  public ConversationApiClient withVersion(@NonNull final String version) {
    config = config.toBuilder().version(version).build();
    return this;
  }

  public AppService getAppService() {
    return new AppService(config);
  }

  public CapabilityService getCapabilityService() {
    return new CapabilityService(config);
  }

  public ContactService getContactService() {
    return new ContactService(config);
  }

  public ConversationService getConversationService() {
    return new ConversationService(config);
  }

  public EventService getEventService() {
    return new EventService(config);
  }

  public MessageService getMessageService() {
    return new MessageService(config);
  }

  public OptInService getOptInService() {
    return new OptInService(config);
  }

  public WebhookService getWebhookService() {
    return new WebhookService(config);
  }

  private static ObjectMapper objectMapper() {
    return new ObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }
}
