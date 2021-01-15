package com.sinch.sdk.model.conversationapi;

import com.sinch.sdk.model.common.Validated;
import com.sinch.sdk.utils.QueryStringBuilder;
import com.sinch.sdk.utils.StringUtils;

/**
 * Builder for List Messages parameters
 *
 * <p>Note: One of conversationId or contactId needs to be set to be valid
 */
public class ListMessagesParams extends Pagination implements Validated {

  protected static final String PARAM_CONVERSATION_ID = "conversation_id";
  protected static final String PARAM_CONTACT_ID = "contact_id";
  protected static final String PARAM_VIEW = "view";

  protected String conversationId;
  protected String contactId;
  protected V1ConversationMessagesView view;

  /**
   * Set the conversation id
   *
   * <p>One of conversation_id or contact_id needs to be present.
   *
   * @param conversationId Resource name (id) of the conversation. (optional)
   * @return {@link ListMessagesParams}
   */
  public ListMessagesParams conversationId(final String conversationId) {
    this.conversationId = conversationId;
    return this;
  }

  /**
   * Set the app id
   *
   * <p>One of conversation_id or contact_id needs to be present.
   *
   * @param contactId The ID of the contact involved in the conversations. (optional)
   * @return {@link ListMessagesParams}
   */
  public ListMessagesParams contactId(final String contactId) {
    this.contactId = contactId;
    return this;
  }

  /**
   * Set the message view
   *
   * @param view (optional, default to WITH_METADATA)
   * @return {@link ListMessagesParams}
   */
  public ListMessagesParams view(final V1ConversationMessagesView view) {
    this.view = view;
    return this;
  }

  @Override
  public ListMessagesParams size(final Integer pageSize) {
    super.size(pageSize);
    return this;
  }

  @Override
  public ListMessagesParams token(final String pageToken) {
    super.token(pageToken);
    return this;
  }

  /** @return True if at least one of conversationId or contactId is set */
  @Override
  public boolean isValid() {
    return !StringUtils.isEmpty(conversationId) || !StringUtils.isEmpty(contactId);
  }

  @Override
  public String build() {
    return QueryStringBuilder.newInstance()
        .add(PARAM_PAGE_SIZE, pageSize)
        .add(PARAM_PAGE_TOKEN, pageToken)
        .add(PARAM_CONVERSATION_ID, conversationId)
        .add(PARAM_CONTACT_ID, contactId)
        .add(PARAM_VIEW, view)
        .build();
  }
}
