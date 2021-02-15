package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.api.conversationapi.restclient.SinchRestClient;
import java.net.URI;

public abstract class AbstractService {

  private static final String API_VERSION = "v1";

  protected static final String PARAMS = "params";
  protected static final String SUB_APP_ID = ".appId";
  protected static final String SUB_CHANNELS = ".channels";

  protected static final String TEMPLATE_URL = "%s/%s/projects/%s/%s";

  protected final String projectId;
  protected final SinchRestClient restClient;
  protected final URI serviceURI;
  private final String serviceUrl;

  public AbstractService(final ConversationApiConfig config) {
    this.projectId = config.getProjectId();
    this.restClient = config.getRestClient();
    serviceUrl =
        String.format(TEMPLATE_URL, config.getBaseUrl(), API_VERSION, projectId, getServiceName());
    serviceURI = URI.create(serviceUrl);
  }

  protected URI withPath(final String path) {
    return URI.create(serviceUrl.concat("/").concat(path));
  }

  protected URI withQuery(final String query) {
    return URI.create(serviceUrl.concat(query));
  }

  protected abstract String getServiceName();
}
