package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.SinchRestClient;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import java.net.URI;

public abstract class AbstractService {

  protected static final String TEMPLATE_URL = "%s/%s/projects/%s/%s";
  protected static final String PAGE_SIZE_PARAM = "page_size";
  protected static final String PAGE_TOKEN_PARAM = "page_token";
  protected static final String CONTACT_PARAM = "contact_id";

  protected final String projectId;
  protected final String version;
  protected final SinchRestClient restClient;

  private final String serviceUrl;
  protected URI serviceURI;

  public AbstractService(final ConversationApiConfig config) {
    this.projectId = config.getProjectId();
    this.version = config.getVersion();
    this.restClient = config.getRestClient();
    serviceUrl =
        String.format(TEMPLATE_URL, config.getBaseUrl(), version, projectId, getServiceName());
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
