package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.BaseTest;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.common.enums.ConversationChannel;
import com.sinch.sdk.model.conversationapi.contact.Contact;
import com.sinch.sdk.model.conversationapi.contact.service.ListContactsResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ContactServiceTest extends BaseTest {

  private static final String appId = "your-app-id";
  private static final String contactId = "your-contact-id";

  private static ContactService contactService;

  @BeforeAll
  static void beforeAll() {
    contactService = Sinch.conversationApi(Region.EU).getContactService();
  }

  @Test
  void testCreateContact() {
    final Contact con =
        Contact.builder()
            .channelIdentities(
                List.of(
                    Contact.ChannelIdentity.builder()
                        .channel(ConversationChannel.MESSENGER)
                        .identity("6536947852974")
                        .appId(appId)
                        .build()))
            .displayName("SDK test contact")
            .build();

    Contact response = contactService.createContact(con);
    prettyPrint(response);
  }

  @Test
  void testUpdateContact() {
    final Contact con =
        Contact.builder()
            .email("email@emial.com")
            .channelIdentities(
                List.of(
                    Contact.ChannelIdentity.builder()
                        .channel(ConversationChannel.MESSENGER)
                        .identity("6536947852974")
                        .appId(appId)
                        .build()))
            .displayName("SDK test contact")
            .build();

    Contact response = contactService.updateContact(con, contactId);
    prettyPrint(response);
  }

  @Test
  void testGetContact() {
    final Contact response = contactService.getContact(contactId);
    prettyPrint(response);
  }

  @Test
  void testDeleteContact() {
    contactService.deleteContact(contactId);
    final Contact response = contactService.getContact(contactId);
    prettyPrint(response);
  }

  @Test
  void testListContacts() {
    final ListContactsResponse response = contactService.listContacts(null, null);
    prettyPrint(response);
  }

  @Test
  public void testListContactsSize() {
    final ListContactsResponse response = contactService.listContacts(3, null);
    prettyPrint(response);
  }
}
