package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.factory.MessageFactory;
import com.sinch.sdk.api.conversationapi.model.ListConversationsParams;
import com.sinch.sdk.api.conversationapi.model.ListMessagesParams;
import com.sinch.sdk.api.conversationapi.service.Conversations;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.AppMessage;
import com.sinch.sdk.model.conversationapi.ChannelIdentity;
import com.sinch.sdk.model.conversationapi.Conversation;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.ConversationDirection;
import com.sinch.sdk.model.conversationapi.ConversationMessage;
import com.sinch.sdk.model.conversationapi.ListConversationsResponse;
import com.sinch.sdk.model.conversationapi.ListMessagesResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class ConversationsExamples {

  private static final String CONVERSATION_ID = "__conversation_id__";

  private static Conversations conversations;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    conversations = Sinch.conversationApi(Region.EU).conversations();
  }

  @Test
  void createConversation() {
    final Conversation response =
        conversations.create(
            new Conversation()
                .active(true)
                .activeChannel(ConversationChannel.MESSENGER)
                .appId(AppsExamples.APP_ID)
                .contactId(ContactsExamples.CONTACT_ID));
    log.info("{}", response);
  }

  @Test
  void deleteConversation() {
    conversations.delete(CONVERSATION_ID);
  }

  @Test
  void getConversation() {
    final Conversation conversation = conversations.get(CONVERSATION_ID);
    log.info("{}", conversation);
  }

  @Test
  void injectMessage() {
    conversations.injectMessage(
        new ConversationMessage()
            .contactId(ContactsExamples.CONTACT_ID)
            .conversationId(CONVERSATION_ID)
            .direction(ConversationDirection.TO_CONTACT)
            .channelIdentity(new ChannelIdentity().channel(ConversationChannel.MESSENGER))
            .appMessage(new AppMessage().textMessage(MessageFactory.textMessage("Hi"))));
  }

  @Test
  void listConversations() {
    final ListConversationsResponse response =
        conversations.list(
            new ListConversationsParams().onlyActive(true).appId(AppsExamples.APP_ID).size(1));
    log.info("{}", response);
  }

  @Test
  void listMessages() {
    final ListMessagesResponse response =
        conversations.listMessages(new ListMessagesParams().conversationId(CONVERSATION_ID));
    log.info("{}", response);
  }

  @Test
  void updateConversation() {
    final Conversation conversation =
        conversations.update(CONVERSATION_ID, new Conversation().metadata("some meta data"));
    log.info("{}", conversation);
  }

  @Test
  void stopActiveConversation() {
    conversations.stopActive(CONVERSATION_ID);
    final Conversation conversation = conversations.get(CONVERSATION_ID);
    log.info("{}", conversation);
  }
}
