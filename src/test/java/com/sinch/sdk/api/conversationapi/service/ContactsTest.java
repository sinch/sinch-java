package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;

import com.sinch.sdk.api.conversationapi.model.Pagination;
import com.sinch.sdk.model.conversationapi.Contact;
import com.sinch.sdk.model.conversationapi.ListContactsResponse;
import com.sinch.sdk.model.conversationapi.MergeContactRequest;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ContactsTest extends BaseServiceTest {

  private static final String CONTACT_ID = "contact-id";

  private static Contacts contacts;

  @BeforeEach
  void setUp() {
    contacts = new Contacts(PROJECT_ID, restClient, BASE_URL);
  }

  @Test
  void publicConstructor() {
    final Contacts contacts = new Contacts(CONFIG, null);
    assertThat(contacts.restClient).isNotNull();
    assertThat(contacts.serviceURI.toString())
        .isEqualTo(String.format(EXPECTED_SERVICE_URI_FORMAT, contacts.getServiceName()));
  }

  @Test
  void createContact() {
    contacts.create(new Contact());

    verifyPostCalled(() -> eq(contacts.serviceURI), Contact.class);
  }

  @Test
  void deleteContact() {
    contacts.delete(CONTACT_ID);

    verifyDeleteCalled(() -> uriPathEndsWithMatcher(CONTACT_ID));
  }

  @Test
  void getContact() {
    contacts.get(CONTACT_ID);

    verifyGetCalled(() -> uriPathEndsWithMatcher(CONTACT_ID), Contact.class);
  }

  @Test
  void listContacts() {
    contacts.list();

    verifyGetCalled(() -> eq(contacts.serviceURI), ListContactsResponse.class);
  }

  @Test
  void listContactsPagination() {
    contacts.list(new Pagination());

    verifyGetCalled(() -> eq(contacts.serviceURI), ListContactsResponse.class);
  }

  @Test
  void listContactsThrows() {
    givenGetThrows();

    //noinspection ThrowableNotThrown
    verifyThrowsApiException(() -> contacts.list());
  }

  @Test
  void mergeContact() {
    contacts.merge(
        new MergeContactRequest().destinationId(CONTACT_ID).sourceId("source-contact-id"));

    verifyPostCalled(
        () -> uriPathEndsWithMatcher(CONTACT_ID + ":merge"),
        Contact.class,
        () -> argThat((MergeContactRequest req) -> PROJECT_ID.equals(req.getProjectId())));
  }

  @Test
  void updateContact() {
    contacts.update(CONTACT_ID, new Contact());

    verifyPatchCalled(() -> uriPathEndsWithMatcher(CONTACT_ID), Contact.class);
  }

  @ParameterizedTest
  @MethodSource("callsWithMissingParams")
  void missingParamsThrows(final ThrowableAssert.ThrowingCallable throwingCallable) {
    assertClientSideException(throwingCallable);
  }

  private static List<ThrowableAssert.ThrowingCallable> callsWithMissingParams() {
    return Arrays.asList(
        () -> contacts.create(null),
        () -> contacts.delete(null),
        () -> contacts.get(null),
        () -> contacts.list(null),
        () -> contacts.merge(null),
        () -> contacts.merge(new MergeContactRequest().sourceId(CONTACT_ID)),
        () -> contacts.merge(new MergeContactRequest().destinationId(CONTACT_ID)),
        () -> contacts.update(null, new Contact()),
        () -> contacts.update(CONTACT_ID, null));
  }
}
