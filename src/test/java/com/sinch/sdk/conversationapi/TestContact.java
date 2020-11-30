package com.sinch.sdk.conversationapi;

import com.sinch.sdk.api.conversationapi.ConversationApiClient;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.contact.Contact;
import com.sinch.sdk.model.conversationapi.contact.Contact.ChannelIdentity;
import com.sinch.sdk.model.conversationapi.contact.service.ListContactsResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestContact extends AbstractTest {
  private static final String contactId = "your-contact-id";
  private static final String appId = "your-app-id";

  @Test
  public void testGetContact() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Contact response = client.getContactService().getContact(contactId);
    System.out.println(response);
  }

  @Test
  public void testCreateContact() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Contact con =
        Contact.builder()
            .channelIdentities(
                List.of(
                    ChannelIdentity.builder()
                        .channel(ConversationChannel.MESSENGER)
                        .identity("6536947852974")
                        .appId(appId)
                        .build()))
            .displayName("SDK test contact")
            .build();

    Contact response = client.getContactService().createContact(con);
    System.out.println(response);
  }

  @Test
  public void testUpdateContact() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    Contact con =
        Contact.builder()
            .email("email@emial.com")
            .channelIdentities(
                List.of(
                    ChannelIdentity.builder()
                        .channel(ConversationChannel.MESSENGER)
                        .identity("6536947852974")
                        .appId(appId)
                        .build()))
            .displayName("SDK test contact")
            .build();

    Contact response = client.getContactService().updateContact(con, contactId);
    System.out.println(response);
  }

  @Test
  public void testDeleteContact() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    client.getContactService().deleteContact(contactId);

    Contact response = client.getContactService().getContact(contactId);
    System.out.println(response);
  }

  @Test
  public void testListContact() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    ListContactsResponse response = client.getContactService().listContacts(null, null);
    System.out.println(response);
  }

  @Test
  public void testListContactsSize() {
    ConversationApiClient client = new ConversationApiClient();
    client.initContext(baseUrl, version, projectId);
    client.initBasicAuth(clientId, clientSecret);

    ListContactsResponse response = client.getContactService().listContacts(3, null);
    System.out.println(response);
  }
}
