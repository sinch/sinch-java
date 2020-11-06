package com.sinch.sdk.model.conversationapi.webhook.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinch.sdk.model.conversationapi.webhook.Webhook;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListWebhooksResponse {
  // List of webhooks belonging to a specific project ID and app ID
  private List<Webhook> webhooks;
}
