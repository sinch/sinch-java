package com.sinch.sdk.api.conversationapi.impl;

import com.sinch.sdk.api.conversationapi.ContactService;
import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.model.conversationapi.contact.Contact;
import com.sinch.sdk.model.conversationapi.contact.service.ListContactsResponse;
import com.sinch.sdk.model.conversationapi.contact.service.MergeContactRequest;
import com.sinch.sdk.utils.QueryStringBuilder;
import java.util.function.Supplier;
import javax.validation.Valid;

public class ContactServiceImpl extends ConversationApiService implements ContactService {
  private static final String URL_TEMPLATE = "%s/%s/projects/%s/contacts";
  private static final String PAGE_SIZE_PARAM = "page_size";
  private static final String PAGE_TOKEN_PARAM = "page_token";

  public ContactServiceImpl(ConversationApiConfig config, Supplier<String> authorizationHeader) {
    super(
        String.format(
            URL_TEMPLATE, config.getBaseUrl(), config.getVersion(), config.getProjectId()),
        authorizationHeader);
  }

  @Override
  public Contact createContact(@Valid Contact contact) {
    return postRequest("", Contact.class, contact);
  }

  @Override
  public Contact updateContact(@Valid Contact contact, String contactId) {
    return patchRequest("/".concat(contactId), Contact.class, contact);
  }

  @Override
  public Contact getContact(String contactId) {
    return getRequest("/".concat(contactId), Contact.class);
  }

  @Override
  public void deleteContact(String contactId) {
    deleteRequest("/".concat(contactId));
  }

  @Override
  public ListContactsResponse listContacts(Integer pageSize, String pageToken) {
    final String queryString =
        QueryStringBuilder.newInstance()
            .add(PAGE_SIZE_PARAM, pageSize)
            .add(PAGE_TOKEN_PARAM, pageToken)
            .build();
    return getRequest(queryString, ListContactsResponse.class);
  }

  // TODO
  @Override
  public Contact mergeContact(@Valid MergeContactRequest mergeContactRequest) {
    return null;
  }
}
