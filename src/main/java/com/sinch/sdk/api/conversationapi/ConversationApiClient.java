package com.sinch.sdk.api.conversationapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.service.*;
import com.sinch.sdk.configuration.Configuration;
import com.sinch.sdk.model.common.Region;
import java.net.http.HttpClient;

public class ConversationApiClient {

  private final Region region;
  private final ConversationApiConfig config;

  public ConversationApiClient(final Region region, final Sinch.Config sinchConfig) {
    this(region, createConfig(region, sinchConfig));
  }

  public ConversationApiClient(final Region region, final ConversationApiConfig config) {
    this.region = region;
    this.config = config;
  }

  private static ConversationApiConfig createConfig(Region region, Sinch.Config sinchConfig) {
    final HttpClient httpClient = HttpClient.newHttpClient();
    final ObjectMapper objectMapper = objectMapper();
    final Configuration regionConfig = Configuration.forRegion(region);
    final AuthenticationService authenticationService =
        new AuthenticationService(
            httpClient,
            regionConfig.authentication(),
            sinchConfig.getKeyId(),
            sinchConfig.getKeySecret());

    return ConversationApiConfig.builder()
        .projectId(sinchConfig.getProjectId())
        .baseUrl(regionConfig.conversationApi().getUrl())
        .restClient(new SinchRestClient(authenticationService, httpClient, objectMapper))
        .build();
  }

  public Region region() {
    return region;
  }

  public AppService apps() {
    return new AppService(config);
  }

  public CapabilityService capabilities() {
    return new CapabilityService(config);
  }

  public ContactService contacts() {
    return new ContactService(config);
  }

  public ConversationService conversations() {
    return new ConversationService(config);
  }

  public EventService events() {
    return new EventService(config);
  }

  public MessageService messages() {
    return new MessageService(config);
  }

  public OptInService optIns() {
    return new OptInService(config);
  }

  public TranscodingService transcoding() {
    return new TranscodingService(config);
  }

  public WebhookService webhooks() {
    return new WebhookService(config);
  }

  private static ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }
}
