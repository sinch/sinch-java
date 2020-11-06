package com.sinch.sdk.api.conversationapi;

import com.sinch.sdk.model.conversationapi.contact.Contact;
import com.sinch.sdk.model.conversationapi.contact.service.ListContactsResponse;
import com.sinch.sdk.model.conversationapi.contact.service.MergeContactRequest;
import java.time.Instant;
import javax.validation.Valid;

public interface ContactService {
  Contact createContact(@Valid Contact contact);

  Contact updateContact(@Valid Contact contact, String contactId);

  Contact getContact(String contactId);

  void deleteContact(String contactId);

  ListContactsResponse listContacts(Integer pageSize, String pageToken);

  Contact mergeContact(@Valid MergeContactRequest mergeContactRequest);
}
