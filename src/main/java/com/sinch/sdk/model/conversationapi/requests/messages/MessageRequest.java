package com.sinch.sdk.model.conversationapi.requests.messages;

import static com.sinch.sdk.api.conversationapi.factories.RecipientFactory.*;

import com.sinch.sdk.model.conversationapi.Recipient;
import com.sinch.sdk.model.conversationapi.SendMessageRequest;

public interface MessageRequest<Request, Message> {

  SendMessageRequest getRequest();

  /**
   * Required. The ID of the app sending the message.
   *
   * <p>NOTE: optional if set on {@link com.sinch.sdk.api.conversationapi.service.Messages}
   *
   * @param appId the app id
   * @return this
   */
  Request appId(final String appId);

  /**
   * Use this function to add a custom recipient.
   *
   * @param recipient the recipient
   * @return this
   */
  Request recipient(final Recipient recipient);

  /**
   * Gets the message from the request, allows for advanced customisation.
   *
   * @return The message
   */
  Message getMessage();

  /**
   * Specifies the recipient by contact ID.
   *
   * @param contactId The contact ID
   * @return this
   */
  default Request contactRecipient(final String contactId) {
    return recipient(fromContactId(contactId));
  }

  /**
   * Specifies the recipient by SMS channel identity.
   *
   * @param identity The sms identity
   * @return this
   */
  default Request smsRecipient(final String identity) {
    return recipient(fromSmsIdentity(identity));
  }

  /**
   * Specifies the recipient by RCS channel identity.
   *
   * @param identity The rcs identity
   * @return this
   */
  default Request rcsRecipient(final String identity) {
    return recipient(fromRcsIdentity(identity));
  }

  /**
   * Specifies the recipient by VIBER channel identity.
   *
   * @param identity The viber identity
   * @return this
   */
  default Request viberRecipient(final String identity) {
    return recipient(fromViberIdentity(identity));
  }

  /**
   * Specifies the recipient by VIBER Business channel identity.
   *
   * @param identity The viber business identity
   * @return this
   */
  default Request viberBMRecipient(final String identity) {
    return recipient(fromViberBMIdentity(identity));
  }

  /**
   * Specifies the recipient by MESSENGER channel identity.
   *
   * @param identity The messenger identity
   * @return this
   */
  default Request messengerRecipient(final String identity) {
    return recipient(fromMessengerIdentity(identity));
  }

  /**
   * Specifies the recipient by WHATSAPP channel identity.
   *
   * @param identity The whatsapp identity
   * @return this
   */
  default Request whatsappRecipient(final String identity) {
    return recipient(fromWhatsappIdentity(identity));
  }
}
