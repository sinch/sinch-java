package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.impl.AppServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.CapabilityServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.ContactServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.ConversationServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.EventServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.MessageServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.OptInServiceImpl;
import com.sinch.sdk.api.conversationapi.impl.WebhookServiceImpl;
import com.sinch.sdk.client.auth.AuthMode;
import com.sinch.sdk.client.auth.module.AuthenticationModule;

public class ConversationApiClient {
  private AuthenticationModule authenticationModule;
  private ConversationApiConfig config;

  public ConversationApiClient() {
    authenticationModule = new AuthenticationModule();
    config = new ConversationApiConfig();
  }

  public ConversationApiClient(final Sinch.Config sinchConfig) {
    this();
    config.setProjectId(sinchConfig.getProjectId());
  }

  public void initBasicAuth(String clientId, String clientSecret) {
    authenticationModule.setAuthMode(AuthMode.BASIC_AUTH, clientId, clientSecret);
  }

  public void initBearerToken(String clientId, String clientSecret) {
    authenticationModule.setAuthMode(AuthMode.BEARER_TOKEN, clientId, clientSecret);
  }

  public void initContext() {
    // TODO solution for props from sysprop, config?
  }

  public void initContext(String baseUrl, String version, String projectId) {
    config.setBaseUrl(baseUrl);
    config.setProjectId(projectId);
    config.setVersion(version);
  }

  public AppService getAppService() {
    return new AppServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }

  public CapabilityService getCapabilityService() {
    return new CapabilityServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }

  public ContactService getContactService() {
    return new ContactServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }

  public ConversationService getConversationService() {
    return new ConversationServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }

  public EventService getEventService() {
    return new EventServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }

  public MessageService getMessageService() {
    return new MessageServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }

  public OptInService getOptInService() {
    return new OptInServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }

  public WebhookService getWebhookService() {
    return new WebhookServiceImpl(config, authenticationModule::getAuthorizationHeader);
  }
}
