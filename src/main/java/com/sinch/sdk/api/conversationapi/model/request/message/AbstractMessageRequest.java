package com.sinch.sdk.api.conversationapi.model.request.message;

import com.sinch.sdk.model.conversationapi.AppMessage;
import com.sinch.sdk.model.conversationapi.ChannelIdentities;
import com.sinch.sdk.model.conversationapi.ChannelRecipientIdentity;
import com.sinch.sdk.model.conversationapi.Recipient;
import com.sinch.sdk.model.conversationapi.SendMessageRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    final Recipient existingRecipient = request.getRecipient();
    if (existingRecipient == null) {
      request.setRecipient(recipient);
    } else {
      final String contactId = recipient.getContactId();
      if (contactId != null) {
        existingRecipient.contactId(contactId);
      }
      final ChannelIdentities existingIdentifiedBy = existingRecipient.getIdentifiedBy();
      if (existingIdentifiedBy == null) {
        existingRecipient.setIdentifiedBy(recipient.getIdentifiedBy());
      } else {
        existingIdentifiedBy.setChannelIdentities(
            Stream.concat(safeStream(existingIdentifiedBy), safeStream(recipient.getIdentifiedBy()))
                .collect(Collectors.toList()));
      }
    }
    return (T) this;
  }

  private Stream<ChannelRecipientIdentity> safeStream(final ChannelIdentities identifiedBy) {
    return Optional.ofNullable(identifiedBy)
        .map(ChannelIdentities::getChannelIdentities)
        .map(List::stream)
        .orElse(Stream.empty());
  }

  protected AppMessage getAppMessage() {
    return getRequest().getMessage();
  }

  @Override
  public SendMessageRequest getRequest() {
    return request;
  }
}
