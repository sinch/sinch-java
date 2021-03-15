package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.service.Apps;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.App;
import com.sinch.sdk.model.conversationapi.ConversationChannel;
import com.sinch.sdk.model.conversationapi.ConversationChannelCredential;
import com.sinch.sdk.model.conversationapi.StaticBearerCredential;
import com.sinch.sdk.model.conversationapi.StaticTokenCredential;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
class AppsExamples {

  static final String APP_ID = "__app_id__";

  private static Apps apps;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    apps = Sinch.conversationApi(Region.EU).apps();
  }

  @Test
  void createApp() {
    final App app =
        apps.create(
            new App()
                .displayName("SDK application name")
                .addChannelCredentialsItem(
                    new ConversationChannelCredential()
                        .channel(ConversationChannel.MESSENGER)
                        .staticToken(new StaticTokenCredential().token("__messenger_token__")))
                .addChannelCredentialsItem(
                    new ConversationChannelCredential()
                        .channel(ConversationChannel.RCS)
                        .staticBearer(
                            new StaticBearerCredential()
                                .claimedIdentity("_rcs_auth_id___")
                                .token("__rcs_token__"))));
    log.info("{}", app);
  }

  @Test
  void deleteApp() {
    apps.delete(APP_ID);
  }

  @Test
  void getApp() {
    final App app = apps.get(APP_ID);
    log.info("{}", app);
  }

  @Test
  void listApps() {
    final List<App> appList = apps.list();
    log.info("{}", appList);
  }

  @Test
  void updateApp() {
    final App app = apps.update(APP_ID, new App().displayName("Updated SDK application name"));
    log.info("{}", app);
  }
}
