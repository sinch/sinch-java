package com.sinch.sdk.model.conversationapi.requests.messages;

import com.sinch.sdk.model.conversationapi.AppMessage;
import com.sinch.sdk.model.conversationapi.Recipient;
import com.sinch.sdk.model.conversationapi.SendMessageRequest;

@SuppressWarnings("unchecked")
public abstract class AbstractMessageRequest<T, R> implements MessageRequest<T, R> {

  protected final SendMessageRequest request;

  public AbstractMessageRequest() {
    request = new SendMessageRequest().message(new AppMessage());
  }

  @Override
  public T appId(final String appId) {
    request.setAppId(appId);
    return (T) this;
  }

  @Override
  public T recipient(final Recipient recipient) {
    request.setRecipient(recipient);
    return (T) this;
  }

  protected AppMessage getAppMessage() {
    return getRequest().getMessage();
  }

  @Override
  public SendMessageRequest getRequest() {
    return request;
  }
}
