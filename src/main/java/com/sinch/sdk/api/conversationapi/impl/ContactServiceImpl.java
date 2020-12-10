package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ContactService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.contact.Contact;
import com.sinch.sdk.model.conversationapi.contact.service.ListContactsResponse;
import com.sinch.sdk.model.conversationapi.contact.service.MergeContactRequest;
import com.sinch.sdk.utils.QueryStringBuilder;
import javax.validation.Valid;

public class ContactServiceImpl extends ConversationApiService implements ContactService {

  public ContactServiceImpl(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "contacts";
  }

  @Override
  public Contact createContact(@Valid final Contact contact) {
    return restClient.post(serviceURI, Contact.class, contact);
  }

  @Override
  public Contact updateContact(@Valid final Contact contact, final String contactId) {
    return restClient.patch(withPath(contactId), Contact.class, contact);
  }

  @Override
  public Contact getContact(final String contactId) {
    return restClient.get(withPath(contactId), Contact.class);
  }

  @Override
  public void deleteContact(final String contactId) {
    restClient.delete(withPath(contactId));
  }

  @Override
  public ListContactsResponse listContacts(final Integer pageSize, final String pageToken) {
    final String queryString =
        QueryStringBuilder.newInstance()
            .add(PAGE_SIZE_PARAM, pageSize)
            .add(PAGE_TOKEN_PARAM, pageToken)
            .build();
    return restClient.get(withQuery(queryString), ListContactsResponse.class);
  }

  // TODO
  @Override
  public Contact mergeContact(@Valid final MergeContactRequest mergeContactRequest) {
    return null;
  }
}
