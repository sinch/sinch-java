package com.sinch.sdk.api.conversationapi.factory;

import com.sinch.sdk.model.conversationapi.ChannelIdentities;
import com.sinch.sdk.model.conversationapi.ChannelRecipientIdentity;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.Recipient;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RecipientFactory {

  public static Recipient fromContactId(final String id) {
    return new Recipient().contactId(id);
  }

  public static Recipient fromSmsIdentity(final String identity) {
    return fromIdentity(ConversationChannel.SMS, identity);
  }

  public static Recipient fromRcsIdentity(final String identity) {
    return fromIdentity(ConversationChannel.RCS, identity);
  }

  public static Recipient fromViberIdentity(final String identity) {
    return fromIdentity(ConversationChannel.VIBER, identity);
  }

  public static Recipient fromViberBMIdentity(final String identity) {
    return fromIdentity(ConversationChannel.VIBERBM, identity);
  }

  public static Recipient fromMessengerIdentity(final String identity) {
    return fromIdentity(ConversationChannel.MESSENGER, identity);
  }

  public static Recipient fromWhatsappIdentity(final String identity) {
    return fromIdentity(ConversationChannel.WHATSAPP, identity);
  }

  public static Recipient fromKakaoTalkIdentity(final String identity) {
    return fromIdentity(ConversationChannel.KAKAOTALK, identity);
  }

  public static Recipient fromInstagramIdentity(final String identity) {
    return fromIdentity(ConversationChannel.INSTAGRAM, identity);
  }

  public static Recipient fromTelegramIdentity(final String identity) {
    return fromIdentity(ConversationChannel.TELEGRAM, identity);
  }

  public static Recipient fromIdentity(final ConversationChannel channel, final String identity) {
    return new Recipient()
        .identifiedBy(
            new ChannelIdentities()
                .addChannelIdentitiesItem(
                    new ChannelRecipientIdentity().identity(identity).channel(channel)));
  }
}
