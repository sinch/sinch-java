package com.sinch.sdk.model.conversationapi.requests.messages;

import com.sinch.sdk.api.conversationapi.factories.MessageFactory;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.TemplateMessage;
import com.sinch.sdk.model.conversationapi.TemplateReference;
import lombok.NonNull;

public class TemplateMessageRequest
    extends AbstractMessageRequest<TemplateMessageRequest, TemplateMessage> {

  /** Creates a template message request */
  public TemplateMessageRequest() {
    getAppMessage().setTemplateMessage(MessageFactory.templateMessage());
  }

  /**
   * Adds a channel specific template reference with parameters per channel. The channel template
   * overrides the omnichannel template. At least one of channel_template or omni_template needs to
   * be present.
   *
   * @param channel The channel
   * @param template The template
   * @return this
   */
  public TemplateMessageRequest channelTemplateItem(
      @NonNull final ConversationChannel channel, final TemplateReference template) {
    getMessage().putChannelTemplateItem(channel.getValue(), template);
    return this;
  }

  /**
   * The fallback template
   *
   * @param template The template
   * @return this
   */
  public TemplateMessageRequest omniTemplate(final TemplateReference template) {
    getMessage().setOmniTemplate(template);
    return this;
  }

  @Override
  public TemplateMessage getMessage() {
    return getAppMessage().getTemplateMessage();
  }
}
