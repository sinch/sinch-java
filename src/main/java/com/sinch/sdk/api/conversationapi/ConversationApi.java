package com.sinch.sdk.api.conversationapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.api.conversationapi.service.Apps;
import com.sinch.sdk.api.conversationapi.service.Capabilities;
import com.sinch.sdk.api.conversationapi.service.Contacts;
import com.sinch.sdk.api.conversationapi.service.Conversations;
import com.sinch.sdk.api.conversationapi.service.Events;
import com.sinch.sdk.api.conversationapi.service.Messages;
import com.sinch.sdk.api.conversationapi.service.OptIns;
import com.sinch.sdk.api.conversationapi.service.Transcoding;
import com.sinch.sdk.api.conversationapi.service.Webhooks;
import com.sinch.sdk.configuration.Configuration;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.restclient.SinchRestClient;
import com.sinch.sdk.restclient.SinchRestClientFactory;

public class ConversationApi {

  private final Region region;
  private final ConversationApiConfig config;
  private final AuthenticationService authenticationService;

  public ConversationApi(
      final Region region,
      final SinchRestClientFactory restClientFactory,
      final Sinch.Config sinchConfig) {
    Configuration regionConfig = Configuration.forRegion(region);
    SinchRestClient restClient =
        restClientFactory.getClient(regionConfig.httpTimeout(), objectMapper());
    this.region = region;
    this.config =
        ConversationApiConfig.builder()
            .projectId(sinchConfig.getProjectId())
            .baseUrl(regionConfig.conversationApi().getUrl())
            .restClient(restClient)
            .build();
    this.authenticationService =
        new AuthenticationService(
            restClient,
            regionConfig.authentication(),
            sinchConfig.getKeyId(),
            sinchConfig.getKeySecret());
  }

  public ConversationApi(
      final Region region,
      final ConversationApiConfig config,
      AuthenticationService authenticationService) {
    this.region = region;
    this.config = config;
    this.authenticationService = authenticationService;
  }

  public Region region() {
    return region;
  }

  public Apps apps() {
    return new Apps(config, authenticationService);
  }

  public Capabilities capabilities() {
    return new Capabilities(config, authenticationService);
  }

  public Contacts contacts() {
    return new Contacts(config, authenticationService);
  }

  public Conversations conversations() {
    return new Conversations(config, authenticationService);
  }

  public Events events() {
    return new Events(config, authenticationService);
  }

  public Messages messages() {
    return new Messages(config, authenticationService);
  }

  public Messages messages(final String appId) {
    return new Messages(config, authenticationService, appId);
  }

  public OptIns optIns() {
    return new OptIns(config, authenticationService);
  }

  public Transcoding transcoding() {
    return new Transcoding(config, authenticationService);
  }

  public Webhooks webhooks() {
    return new Webhooks(config, authenticationService);
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
