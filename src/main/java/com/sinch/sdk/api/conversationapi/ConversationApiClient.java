package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.api.conversationapi.impl.AppServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.CapabilityServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.ContactServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.ConversationServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.EventServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.MessageServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.OptInServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.WebhookServiceImpl;
import com.sinch.sdk.model.common.Region;
import java.util.Optional;
import javax.naming.ConfigurationException;
import lombok.NonNull;
import lombok.SneakyThrows;

public class ConversationApiClient {

  private ConversationApiConfig config;

  public ConversationApiClient(final ConversationApiConfig config) {
    this.config =
        config.toBuilder().region(Region.safeValueOf(System.getProperty("sinch.region"))).build();
  }

  public ConversationApiClient forRegion(@NonNull final Region region) {
    config = config.toBuilder().region(region).build();
    return this;
  }

  public ConversationApiClient forVersion(@NonNull final String version) {
    config = config.toBuilder().version(version).build();
    return this;
  }

  public AppService getAppService() {
    validate();
    return new AppServiceImpl(config);
  }

  public CapabilityService getCapabilityService() {
    validate();
    return new CapabilityServiceImpl(config);
  }

  public ContactService getContactService() {
    validate();
    return new ContactServiceImpl(config);
  }

  public ConversationService getConversationService() {
    validate();
    return new ConversationServiceImpl(config);
  }

  public EventService getEventService() {
    validate();
    return new EventServiceImpl(config);
  }

  public MessageService getMessageService() {
    validate();
    return new MessageServiceImpl(config);
  }

  public OptInService getOptInService() {
    validate();
    return new OptInServiceImpl(config);
  }

  public WebhookService getWebhookService() {
    validate();
    return new WebhookServiceImpl(config);
  }

  @SneakyThrows
  private void validate() {
    Optional.ofNullable(config.getRegion())
        .orElseThrow(
            () ->
                new ConfigurationException(
                    "ConversationApi requires a region, set using .forRegion(...)"));
  }
}
