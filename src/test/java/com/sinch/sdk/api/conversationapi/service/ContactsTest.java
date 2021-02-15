package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ContactsTest extends BaseConvIntegrationTest {

  private static final String contactId = "your-contact-id";

  private static Contacts contacts;

  @BeforeAll
  static void beforeAll() {
    contacts = Sinch.conversationApi(Region.EU).contacts();
  }

  @Test
  void testCreateContact() {
    final Contact contact =
        contacts.create(
            new Contact()
                .displayName("SDK test contact")
                .addChannelIdentitiesItem(
                    new ChannelIdentity()
                        .channel(ConversationChannel.MESSENGER)
                        .identity("6536947852974")
                        .appId("your-app-id")));

    prettyPrint(contact);
  }

  @Test
  void testDeleteContact() {
    contacts.delete(contactId);
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> contacts.get(contactId));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
    Assertions.assertEquals(
        Optional.of("404"), exception.getResponseHeaders().firstValue(":status"));
    System.out.println(exception.getResponseBody());
  }

  @Test
  void testGetContact() {
    final Contact contact = contacts.get(contactId);
    prettyPrint(contact);
  }

  @Test
  void testListContacts() {
    final ListContactsResponse response = contacts.list();
    prettyPrint(response);
  }

  @Test
  public void testListContactsSize() {
    final ListContactsResponse response = contacts.list(new Pagination().size(1));
    prettyPrint(response);
  }

  @Test
  public void testListContactsToken() {
    final ListContactsResponse response = contacts.list(new Pagination().token("nextPageToken"));
    prettyPrint(response);
  }

  @Test
  void testMergeContact() {
    final Contact contact =
        contacts.merge(
            new MergeContactRequest().destinationId(contactId).sourceId("second-contact-id"));
    prettyPrint(contact);
  }

  @Test
  void testUpdateContact() {
    final Contact contact =
        contacts.update(
            contactId, new Contact().displayName("Updated test contact").email("email@emial.com"));
    prettyPrint(contact);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> contacts.create(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contacts.delete(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contacts.get(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contacts.list(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contacts.merge(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> contacts.merge(new MergeContactRequest().sourceId(contactId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> contacts.merge(new MergeContactRequest().destinationId(contactId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> contacts.update(null, new Contact()));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contacts.update(contactId, null));
    assertClientSideException(exception);
  }
}
