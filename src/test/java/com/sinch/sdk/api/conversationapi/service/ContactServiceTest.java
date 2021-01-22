package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.common.Region;
import com.sinch.sdk.model.conversationapi.*;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ContactServiceTest extends BaseConvIntegrationTest {

  private static final String contactId = "your-contact-id";

  private static ContactService contactService;

  @BeforeAll
  static void beforeAll() {
    contactService = Sinch.conversationApi(Region.EU).contacts();
  }

  @Test
  void testCreateContact() throws ApiException {
    final Contact contact =
        contactService.create(
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
  void testDeleteContact() throws ApiException {
    contactService.delete(contactId);
    final ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> contactService.get(contactId));
    Assertions.assertEquals(404, exception.getCode());
    Assertions.assertNotNull(exception.getResponseBody());
    Assertions.assertNotNull(exception.getResponseHeaders());
    Assertions.assertEquals(
        Optional.of("404"), exception.getResponseHeaders().firstValue(":status"));
    System.out.println(exception.getResponseBody());
  }

  @Test
  void testGetContact() throws ApiException {
    final Contact contact = contactService.get(contactId);
    prettyPrint(contact);
  }

  @Test
  void testListContacts() throws ApiException {
    final V1ListContactsResponse response = contactService.list();
    prettyPrint(response);
  }

  @Test
  public void testListContactsSize() throws ApiException {
    final V1ListContactsResponse response = contactService.list(new Pagination().size(1));
    prettyPrint(response);
  }

  @Test
  public void testListContactsToken() throws ApiException {
    final V1ListContactsResponse response =
        contactService.list(new Pagination().token("nextPageToken"));
    prettyPrint(response);
  }

  @Test
  void testMergeContact() throws ApiException {
    final Contact contact =
        contactService.merge(
            new V1MergeContactRequest().destinationId(contactId).sourceId("second-contact-id"));
    prettyPrint(contact);
  }

  @Test
  void testUpdateContact() throws ApiException {
    final Contact contact =
        contactService.update(
            contactId, new Contact().displayName("Updated test contact").email("email@emial.com"));
    prettyPrint(contact);
  }

  @Test
  void testMissingParamsThrows() {
    ApiException exception =
        Assertions.assertThrows(ApiException.class, () -> contactService.create(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contactService.delete(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contactService.get(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contactService.list(null));
    assertClientSideException(exception);
    exception = Assertions.assertThrows(ApiException.class, () -> contactService.merge(null));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> contactService.merge(new V1MergeContactRequest().sourceId(contactId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class,
            () -> contactService.merge(new V1MergeContactRequest().destinationId(contactId)));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(
            ApiException.class, () -> contactService.update(null, new Contact()));
    assertClientSideException(exception);
    exception =
        Assertions.assertThrows(ApiException.class, () -> contactService.update(contactId, null));
    assertClientSideException(exception);
  }
}
