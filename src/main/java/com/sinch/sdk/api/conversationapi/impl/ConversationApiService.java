package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import java.net.URI;

public abstract class ConversationApiService {

  protected static final String TEMPLATE_URL =
      "https://api.%s1tst.conversation-api.staging.sinch.com/%s/projects/%s/%s";
  protected static final String PAGE_SIZE_PARAM = "page_size";
  protected static final String PAGE_TOKEN_PARAM = "page_token";
  protected static final String CONTACT_PARAM = "contact_id";

  protected final String projectId;
  protected final String region;
  protected final String version;
  protected final SinchRestClient restClient;

  private String serviceUrl;
  protected URI serviceURI;

  public ConversationApiService(final ConversationApiConfig config) {
    this.projectId = config.getProjectId();
    this.region = config.getRegion().nameLowercase();
    this.version = config.getVersion();
    this.restClient = config.getRestClient();
    updateServiceUrl();
  }

  protected void updateServiceUrl() {
    serviceUrl = String.format(TEMPLATE_URL, region, version, projectId, getServiceName());
    serviceURI = URI.create(serviceUrl);
  }

  protected URI withPath(final String path) {
    return withPath(serviceUrl, path);
  }

  protected static URI withPath(final String serviceUrl, final String path) {
    return URI.create(serviceUrl.concat("/").concat(path));
  }

  protected URI withQuery(final String query) {
    return URI.create(serviceUrl.concat(query));
  }

  protected abstract String getServiceName();
}
