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

public class ConversationApi {

  private final Region region;
  private final ConversationApiConfig config;

  public ConversationApi(final Region region, final Sinch.Config sinchConfig) {
    this(region, createConfig(region, sinchConfig));
  }

  public ConversationApi(final Region region, final ConversationApiConfig config) {
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

  public Apps apps() {
    return new Apps(config);
  }

  public Capabilities capabilities() {
    return new Capabilities(config);
  }

  public Contacts contacts() {
    return new Contacts(config);
  }

  public Conversations conversations() {
    return new Conversations(config);
  }

  public Events events() {
    return new Events(config);
  }

  public Messages messages() {
    return new Messages(config);
  }

  public OptIns optIns() {
    return new OptIns(config);
  }

  public Transcoding transcoding() {
    return new Transcoding(config);
  }

  public Webhooks webhooks() {
    return new Webhooks(config);
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
