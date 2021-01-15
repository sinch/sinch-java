package com.sinch.sdk.model.conversationapi;

import com.sinch.sdk.model.common.Validated;
import com.sinch.sdk.utils.QueryStringBuilder;
import com.sinch.sdk.utils.StringUtils;

/**
 * Builder for List Conversation parameters
 *
 * <p>Note: One of appId or contactId needs to be set to be valid
 */
public class ListConversationsParams extends Pagination implements Validated {

  protected static final String PARAM_ACTIVE = "active_only";
  protected static final String PARAM_APP_ID = "app_id";
  protected static final String PARAM_CONTACT_ID = "contact_id";

  protected String appId;
  protected String contactId;
  protected Boolean onlyActive;

  /**
   * Set the app id
   *
   * <p>Note: At least one of app_id and contact_id needs to be present.
   *
   * @param appId The ID of the app involved in the conversations. (optional)
   * @return {@link ListConversationsParams}
   */
  public ListConversationsParams appId(final String appId) {
    this.appId = appId;
    return this;
  }

  /**
   * Set the app id
   *
   * <p>Note: At least one of app_id and contact_id needs to be present.
   *
   * @param contactId The ID of the contact involved in the conversations. (optional)
   * @return {@link ListConversationsParams}
   */
  public ListConversationsParams contactId(final String contactId) {
    this.contactId = contactId;
    return this;
  }

  /**
   * Set to only list active conversations
   *
   * @param onlyActive True if only active conversations should be listed. (optional)
   * @return {@link ListConversationsParams}
   */
  public ListConversationsParams onlyActive(final boolean onlyActive) {
    this.onlyActive = onlyActive;
    return this;
  }

  @Override
  public ListConversationsParams size(final Integer pageSize) {
    super.size(pageSize);
    return this;
  }

  @Override
  public ListConversationsParams token(final String pageToken) {
    super.token(pageToken);
    return this;
  }

  /** @return True if at least one of appId or contactId is set */
  @Override
  public boolean isValid() {
    return !StringUtils.isEmpty(appId) || !StringUtils.isEmpty(contactId);
  }

  @Override
  public String build() {
    return QueryStringBuilder.newInstance()
        .add(PARAM_PAGE_SIZE, pageSize)
        .add(PARAM_PAGE_TOKEN, pageToken)
        .add(PARAM_ACTIVE, onlyActive)
        .add(PARAM_APP_ID, appId)
        .add(PARAM_CONTACT_ID, contactId)
        .build();
  }
}
