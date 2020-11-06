package com.sinch.sdk.model.conversationapi.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sinch.sdk.model.conversationapi.conversation.Conversation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationStartNotification {

  private Conversation conversation;
}
