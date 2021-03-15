package example.conversationapi;

import com.sinch.sdk.Sinch;
import com.sinch.sdk.api.conversationapi.service.Webhooks;
import com.sinch.sdk.model.Region;
import com.sinch.sdk.model.conversationapi.Webhook;
import com.sinch.sdk.model.conversationapi.WebhookTargetType;
import com.sinch.sdk.model.conversationapi.WebhookTrigger;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Slf4j
@Tag("example")
public class WebhooksExamples {

  private static final String WEBHOOK_ID = "__webhook_id__";

  private static Webhooks webhooks;

  @BeforeAll
  static void beforeAll() {
    // Initialized from system properties, see README.MD in this folder for details.
    webhooks = Sinch.conversationApi(Region.EU).webhooks();
  }

  @Test
  void createWebhook() {
    final Webhook webhook =
        webhooks.create(
            new Webhook()
                .appId(AppsExamples.APP_ID)
                .target("https://webhook.site/d9cb2b5f-5ecd-4c19-ac34-b059b6e5eae1")
                .addTriggersItem(WebhookTrigger.MESSAGE_DELIVERY)
                .addTriggersItem(WebhookTrigger.EVENT_DELIVERY)
                .addTriggersItem(WebhookTrigger.MESSAGE_INBOUND)
                .addTriggersItem(WebhookTrigger.EVENT_INBOUND)
                .addTriggersItem(WebhookTrigger.CONVERSATION_START)
                .addTriggersItem(WebhookTrigger.CONVERSATION_STOP)
                .addTriggersItem(WebhookTrigger.UNSUPPORTED)
                .targetType(WebhookTargetType.HTTP));
    log.info("{}", webhook);
  }

  @Test
  void deleteWebhook() {
    webhooks.delete(WEBHOOK_ID);
  }

  @Test
  void getWebhook() {
    final Webhook webhook = webhooks.get(WEBHOOK_ID);
    log.info("{}", webhook);
  }

  @Test
  void listWebhooks() {
    final List<Webhook> response = webhooks.list(AppsExamples.APP_ID);
    log.info("{}", response);
  }

  @Test
  void updateWebhook() {
    final Webhook webhook =
        webhooks.update(WEBHOOK_ID, new Webhook().targetType(WebhookTargetType.GRPC));
    log.info("{}", webhook);
  }
}
