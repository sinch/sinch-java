package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityRequest;
import com.sinch.sdk.model.conversationapi.capability.service.QueryCapabilityResponse;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import org.junit.jupiter.api.Test;

public class TestCapability extends AbstractTest{
  private static final String appId = "your-app-id";
  private static final String contactId = "your-contact-id";

  @Test
  public void testQueryCap() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    QueryCapabilityRequest rq =
        QueryCapabilityRequest.builder()
            .appId(appId)
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    QueryCapabilityResponse response = client.getCapabilityService().queryCapability(rq);
    System.out.println(response);
  }
}
