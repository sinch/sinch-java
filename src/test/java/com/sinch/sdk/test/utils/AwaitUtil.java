package com.sinch.sdk.test.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;
import org.awaitility.Awaitility;
import org.awaitility.core.ThrowingRunnable;

@UtilityClass
public class AwaitUtil {

  public void awaitValidAssertion(final ThrowingRunnable runnable) {
    Awaitility.await()
        .atMost(Duration.ofSeconds(5))
        .pollInterval(Duration.ofMillis(250))
        .untilAsserted(runnable);
  }

  public void delay(final Duration duration) {
    Awaitility.await().pollDelay(duration.toMillis(), TimeUnit.MILLISECONDS).until(() -> true);
  }

  public void delaySeconds(int seconds) {
    delay(Duration.ofSeconds(seconds));
  }
}
