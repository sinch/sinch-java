package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class VersionTest {

  private static final Pattern SEM_VER_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.)?(\\d*)");

  @Test
  void versionValueShouldContainValidSemanticVersion() {
    assertThat(Version.getValue()).isNotBlank().isNotEqualTo("undefined").matches(SEM_VER_PATTERN);
  }
}
