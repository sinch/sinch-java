package com.sinch.sdk.api.conversationapi.service;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.model.conversationapi.Contact;
import com.sinch.sdk.model.conversationapi.Pagination;
import com.sinch.sdk.model.conversationapi.V1ListContactsResponse;
import com.sinch.sdk.model.conversationapi.V1MergeContactRequest;
import com.sinch.sdk.utils.ExceptionUtils;
import com.sinch.sdk.utils.StringUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ContactService extends AbstractService {

  static final String PARAM_CONTACT_ID = "contactId";
  private static final String PARAM_CONTACT = "contact";
  private static final String PARAM_MERGE = "mergeRequest";
  private static final String PARAM_MERGE_DESTINATION_ID = PARAM_MERGE + ".destinationId";
  private static final String PARAM_MERGE_SOURCE_ID = PARAM_MERGE + ".sourceId";

  public ContactService(final ConversationApiConfig config) {
    super(config);
  }

  @Override
  protected String getServiceName() {
    return "contacts";
  }

  /**
   * Create a Contact (blocking)
   *
   * @param contact The contact to be added. (required)
   * @return {@link Contact}
   * @throws ApiException if fails to make API call
   */
  public Contact create(final Contact contact) throws ApiException {
    try {
      return createAsync(contact).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Create a Contact
   *
   * @param contact The contact to be added. (required)
   * @return Async task providing a {@link Contact}
   */
  public CompletableFuture<Contact> createAsync(final Contact contact) {
    if (contact == null) {
      return ExceptionUtils.missingParam(PARAM_CONTACT);
    }
    return restClient.post(serviceURI, Contact.class, contact);
  }

  /**
   * Delete a Contact (blocking)
   *
   * @param contactId The ID of the contact to be deleted. (required)
   * @throws ApiException if fails to make API call
   */
  public void delete(final String contactId) throws ApiException {
    try {
      deleteAsync(contactId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Delete a Contact
   *
   * @param contactId The ID of the contact to be deleted. (required)
   * @return Async task of the delete call
   */
  public CompletableFuture<Void> deleteAsync(final String contactId) {
    if (StringUtils.isEmpty(contactId)) {
      return ExceptionUtils.missingParam(PARAM_CONTACT_ID);
    }
    return restClient.delete(withPath(contactId));
  }

  /**
   * Get a Contact (blocking)
   *
   * @param contactId The ID of the contact. (required)
   * @return {@link Contact}
   * @throws ApiException if fails to make API call
   */
  public Contact get(final String contactId) throws ApiException {
    try {
      return getAsync(contactId).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Get a Contact
   *
   * @param contactId The ID of the contact. (required)
   * @return Async task providing a {@link Contact}
   */
  public CompletableFuture<Contact> getAsync(final String contactId) {
    if (StringUtils.isEmpty(contactId)) {
      return ExceptionUtils.missingParam(PARAM_CONTACT_ID);
    }
    return restClient.get(withPath(contactId), Contact.class);
  }

  /**
   * Get contacts (blocking)
   *
   * <p>Will fetch up to 10 contacts.
   *
   * <p>If a nextPageToken is returned use it as the 'token' in {@link
   * ContactService#list(Pagination)}
   *
   * @return {@link V1ListContactsResponse}
   * @throws ApiException if fails to make API call
   */
  public V1ListContactsResponse list() throws ApiException {
    try {
      return listAsync().join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Get contacts (blocking)
   *
   * @param pagination Object specifying the pageSize and pageToken for the list call
   * @return {@link V1ListContactsResponse}
   * @throws ApiException if fails to make API call
   */
  public V1ListContactsResponse list(final Pagination pagination) throws ApiException {
    try {
      return listAsync(pagination).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Get contacts
   *
   * <p>Will fetch up to 10 contacts.
   *
   * <p>If a nextPageToken is returned use it as the 'token' in {@link
   * ContactService#list(Pagination)}
   *
   * @return Async task generating a {@link V1ListContactsResponse}
   */
  public CompletableFuture<V1ListContactsResponse> listAsync() {
    return restClient.get(serviceURI, V1ListContactsResponse.class);
  }

  /**
   * Get contacts
   *
   * @param pagination Object specifying the pageSize and pageToken for the list call
   * @return Async task generating a {@link V1ListContactsResponse}
   */
  public CompletableFuture<V1ListContactsResponse> listAsync(final Pagination pagination) {
    if (pagination == null) {
      return ExceptionUtils.missingParam("pagination");
    }
    return restClient.get(withQuery(pagination.build()), V1ListContactsResponse.class);
  }

  /**
   * Merge two contacts (blocking)
   *
   * <p>The remaining contact will contain all conversations that the removed contact did. If both
   * contacts had conversations within the same App, messages from the removed contact will be
   * merged into corresponding active conversations in the destination contact. Channel identities
   * will be moved from the source contact to the destination contact only for channels that were
   * not present there before. Moved channel identities will be placed at the bottom of the channel
   * priority list. Optional fields from the source contact will be copied only if corresponding
   * fields in the destination contact are empty The contact being removed cannot be referenced
   * after this call.
   *
   * @param mergeRequest (required)
   * @return {@link Contact}
   * @throws ApiException if fails to make API call
   */
  public Contact merge(final V1MergeContactRequest mergeRequest) throws ApiException {
    try {
      return mergeAsync(mergeRequest).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Merge two contacts
   *
   * <p>The remaining contact will contain all conversations that the removed contact did. If both
   * contacts had conversations within the same App, messages from the removed contact will be
   * merged into corresponding active conversations in the destination contact. Channel identities
   * will be moved from the source contact to the destination contact only for channels that were
   * not present there before. Moved channel identities will be placed at the bottom of the channel
   * priority list. Optional fields from the source contact will be copied only if corresponding
   * fields in the destination contact are empty The contact being removed cannot be referenced
   * after this call.
   *
   * @param mergeRequest (required)
   * @return Async task generating a {@link Contact}
   */
  public CompletableFuture<Contact> mergeAsync(final V1MergeContactRequest mergeRequest) {
    if (mergeRequest == null) {
      return ExceptionUtils.missingParam(PARAM_MERGE);
    }
    final String destinationId = mergeRequest.getDestinationId();
    if (StringUtils.isEmpty(destinationId)) {
      return ExceptionUtils.missingParam(PARAM_MERGE_DESTINATION_ID);
    }
    if (StringUtils.isEmpty(mergeRequest.getSourceId())) {
      return ExceptionUtils.missingParam(PARAM_MERGE_SOURCE_ID);
    }
    return restClient.post(
        withPath(destinationId.concat(":merge")), Contact.class, mergeRequest.projectId(projectId));
  }

  /**
   * Update a Contact (blocking)
   *
   * @param contactId The ID of the contact. (required)
   * @param contact The updated contact. (required)
   * @return {@link Contact}
   * @throws ApiException if fails to make API call
   */
  public Contact update(final String contactId, final Contact contact) throws ApiException {
    try {
      return updateAsync(contactId, contact).join();
    } catch (final CompletionException ex) {
      throw ExceptionUtils.getExpectedCause(ex);
    }
  }

  /**
   * Update a Contact
   *
   * @param contactId The ID of the contact. (required)
   * @param contact The updated contact. (required)
   * @return Async task generating a {@link Contact}
   */
  public CompletableFuture<Contact> updateAsync(final String contactId, final Contact contact) {
    if (StringUtils.isEmpty(contactId)) {
      return ExceptionUtils.missingParam(PARAM_CONTACT_ID);
    }
    if (contact == null) {
      return ExceptionUtils.missingParam(PARAM_CONTACT);
    }
    return restClient.patch(withPath(contactId), Contact.class, contact);
  }
}
