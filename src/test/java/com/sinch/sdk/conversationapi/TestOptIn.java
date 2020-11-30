package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.common.Recipient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.optin.OptIn;
import com.sinch.sdk.model.conversationapi.optin.OptOut;
import com.sinch.sdk.model.conversationapi.optin.service.OptInResponse;
import com.sinch.sdk.model.conversationapi.optin.service.OptOutResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestOptIn extends AbstractTest {
  private static final String contactId = "your-contact-id";
  private static final String appId = "your-app-id";

  @Test
  public void testOptIn() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    OptIn in =
        OptIn.builder()
            .appId(appId)
            .channels(List.of(ConversationChannel.WHATSAPP))
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    OptInResponse response = client.getOptInService().registerOptIn(in);
    System.out.println(response);
  }

  @Test
  public void testOptOut() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    OptOut out =
        OptOut.builder()
            .appId(appId)
            .channels(List.of(ConversationChannel.WHATSAPP))
            .recipient(Recipient.fromContactId().contactId(contactId).build())
            .build();

    OptOutResponse response = client.getOptInService().registerOptOut(out);
    System.out.println(response);
  }
}
