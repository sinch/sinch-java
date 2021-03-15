package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.model.Pagination;
import com.sinch.sdk.api.conversationapi.service.Contacts;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.ChannelIdentity;
import com.sinch.sdk.model.conversationapi.Contact;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.ListContactsResponse;
import com.sinch.sdk.model.conversationapi.MergeContactRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class ContactsExamples {

  static final String CONTACT_ID = "__contact_id__";

  private static Contacts contacts;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    contacts = Sinch.conversationApi(Region.EU).contacts();
  }

  @Test
  void createContact() {
    final Contact contact =
        contacts.create(
            new Contact()
                .displayName("SDK contact name")
                .addChannelIdentitiesItem(
                    new ChannelIdentity()
                        .channel(ConversationChannel.MESSENGER)
                        .identity("2945676228809739")
                        .appId(AppsExamples.APP_ID))
                .addChannelIdentitiesItem(
                    new ChannelIdentity()
                        .channel(ConversationChannel.WHATSAPP)
                        .identity("14707864783"))
                .addChannelIdentitiesItem(
                    new ChannelIdentity().channel(ConversationChannel.RCS).identity("14707864783"))
                .addChannelIdentitiesItem(
                    new ChannelIdentity().channel(ConversationChannel.SMS).identity("46732619989"))
                .addChannelPriorityItem(ConversationChannel.MESSENGER)
                .addChannelPriorityItem(ConversationChannel.RCS)
                .addChannelPriorityItem(ConversationChannel.WHATSAPP)
                .addChannelPriorityItem(ConversationChannel.SMS));

    log.info("{}", contact);
  }

  @Test
  void deleteContact() {
    contacts.delete(CONTACT_ID);
  }

  @Test
  void getContact() {
    final Contact contact = contacts.get(CONTACT_ID);
    log.info("{}", contact);
  }

  @Test
  void listContacts() {
    final ListContactsResponse response = contacts.list();
    log.info("{}", response);
  }

  @Test
  public void listContactsSize() {
    final ListContactsResponse response = contacts.list(new Pagination().size(1));
    log.info("{}", response);
  }

  @Test
  public void listContactsToken() {
    final ListContactsResponse response = contacts.list(new Pagination().token("nextPageToken"));
    log.info("{}", response);
  }

  @Test
  void mergeContact() {
    final Contact contact =
        contacts.merge(
            new MergeContactRequest().destinationId(CONTACT_ID).sourceId("source-contact-id"));
    log.info("{}", contact);
  }

  @Test
  void updateContact() {
    final Contact contact =
        contacts.update(
            CONTACT_ID,
            new Contact().displayName("Updated SDK contact name").email("email@emial.com"));
    log.info("{}", contact);
  }
}
