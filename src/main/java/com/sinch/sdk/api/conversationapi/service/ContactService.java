package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.contact.Contact;
import com.sinch.sdk.model.conversationapi.contact.service.ListContactsResponse;
import com.sinch.sdk.model.conversationapi.contact.service.MergeContactRequest;
import com.sinch.sdk.utils.QueryStringBuilder;
import javax.validation.Valid;

public class ContactService extends AbstractService {

  public ContactService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "contacts";
  }

  public Contact createContact(@Valid final Contact contact) {
    return restClient.post(serviceURI, Contact.class, contact);
  }

  public Contact updateContact(@Valid final Contact contact, final String contactId) {
    return restClient.patch(withPath(contactId), Contact.class, contact);
  }

  public Contact getContact(final String contactId) {
    return restClient.get(withPath(contactId), Contact.class);
  }

  public void deleteContact(final String contactId) {
    restClient.delete(withPath(contactId));
  }

  public ListContactsResponse listContacts(final Integer pageSize, final String pageToken) {
    final String queryString =
        QueryStringBuilder.newInstance()
            .add(PAGE_SIZE_PARAM, pageSize)
            .add(PAGE_TOKEN_PARAM, pageToken)
            .build();
    return restClient.get(withQuery(queryString), ListContactsResponse.class);
  }

  // TODO
  public Contact mergeContact(@Valid final MergeContactRequest mergeContactRequest) {
    return null;
  }
}
