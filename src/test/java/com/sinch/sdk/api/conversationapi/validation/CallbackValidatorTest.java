package com.sinch.sdk.api.conversationapi.validation;

import static com.sinch.sdk.api.conversationapi.validation.CallbackValidator.Exception.Reason.*;

import com.sinch.sdk.test.extension.ResourceExtension;
import com.sinch.sdk.test.extension.ResourceExtension.Resource;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResourceExtension.class)
class CallbackValidatorTest {

  private static final String VALID_SECRET = "ohsosecret";
  private static final Duration VALID_DURATION = Duration.ofDays(365 * 25);

  private String payload;
  private Map<String, String> headers;

  private CallbackValidator validValidator;

  @BeforeEach
  void setUp(
      @Resource("conversation-api/callback-validation/conversation-start-callback.json")
          String json,
      @Resource(
              value =
                  "conversation-api/callback-validation/conversation-start-callback-headers.json",
              type = Map.class)
          Map<String, String> headersFromFile) {
    payload = json;
    headers = headersFromFile;
    validValidator = new CallbackValidator(VALID_SECRET).durationWindow(VALID_DURATION);
  }

  @Test
  void testHappyPath() {
    Assertions.assertTrue(validValidator.validateCallback(payload, headers));
  }

  @Test
  void testMissingSecret() {
    final CompletionException exception =
        Assertions.assertThrows(
            CompletionException.class,
            () -> new CallbackValidator("").validateCallback(payload, headers));
    final CallbackValidator.Exception cause = (CallbackValidator.Exception) exception.getCause();
    Assertions.assertEquals(INVALID_SECRET, cause.getReason());
  }

  @Test
  void testMissingExpectedHeaders() {
    headers.put(CallbackValidator.HEADER_NAME_SIGNATURE, "");
    final CompletionException exception =
        Assertions.assertThrows(
            CompletionException.class,
            () -> new CallbackValidator(VALID_SECRET).validateCallback(payload, headers));
    final CallbackValidator.Exception cause = (CallbackValidator.Exception) exception.getCause();
    Assertions.assertEquals(MISSING_SIGNATURE, cause.getReason());
  }

  @Test
  void testNotInTime() {
    final CompletionException exception =
        Assertions.assertThrows(
            CompletionException.class,
            () -> new CallbackValidator(VALID_SECRET).validateCallback(payload, headers));
    final CallbackValidator.Exception cause = (CallbackValidator.Exception) exception.getCause();
    Assertions.assertEquals(NOT_IN_TIME, cause.getReason());
  }

  @Test
  void testFailedNonce() {
    final Set<String> replayProtectionCache = new HashSet<>();
    final CallbackValidator callbackValidator =
        validValidator.nonceValidator(replayProtectionCache::add);

    Assertions.assertTrue(callbackValidator.validateCallback(payload, headers));

    final CompletionException exception =
        Assertions.assertThrows(
            CompletionException.class, () -> callbackValidator.validateCallback(payload, headers));
    final CallbackValidator.Exception cause = (CallbackValidator.Exception) exception.getCause();
    Assertions.assertEquals(FAILED_NONCE, cause.getReason());
  }

  @Test
  void testFailedOauth2() {
    Assertions.assertTrue(
        validValidator
            .oauth2Validator(token -> token.startsWith("Bearer "))
            .validateCallback(payload, headers));

    final CompletionException exception =
        Assertions.assertThrows(
            CompletionException.class,
            () ->
                validValidator
                    .oauth2Validator(token -> token.endsWith("123kid"))
                    .validateCallback(payload, headers));
    final CallbackValidator.Exception cause = (CallbackValidator.Exception) exception.getCause();
    Assertions.assertEquals(FAILED_OAUTH2, cause.getReason());
  }

  @Test
  void testSignatureMismatch() {
    final CompletionException exception =
        Assertions.assertThrows(
            CompletionException.class,
            () ->
                new CallbackValidator("INVALID_SECRET")
                    .durationWindow(VALID_DURATION)
                    .validateCallback(payload, headers));
    final CallbackValidator.Exception cause = (CallbackValidator.Exception) exception.getCause();
    Assertions.assertEquals(SIGNATURE_MISMATCH, cause.getReason());
  }
}
