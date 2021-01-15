package com.sinch.sdk.model.conversationapi;

import com.sinch.sdk.utils.QueryStringBuilder;

/** Builder for paginated requests */
public class Pagination {

  protected static final String PARAM_PAGE_SIZE = "page_size";
  protected static final String PARAM_PAGE_TOKEN = "page_token";

  protected Integer pageSize;
  protected String pageToken;

  /**
   * Set the page size
   *
   * @param pageSize The maximum number of contacts to fetch. The default is 10 and the maximum is
   *     20. (optional)
   * @return {@link Pagination}
   */
  public Pagination size(final Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Set the page token
   *
   * @param pageToken Next page token previously returned if any. (optional)
   * @return {@link Pagination}
   */
  public Pagination token(final String pageToken) {
    this.pageToken = pageToken;
    return this;
  }

  public String build() {
    return QueryStringBuilder.newInstance()
        .add(PARAM_PAGE_SIZE, pageSize)
        .add(PARAM_PAGE_TOKEN, pageToken)
        .build();
  }
}
