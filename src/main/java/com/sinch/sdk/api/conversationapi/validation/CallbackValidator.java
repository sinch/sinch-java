package com.sinch.sdk.api.conversationapi.validation;

import com.sinch.sdk.api.authentication.AuthenticationService;
import com.sinch.sdk.util.StringUtils;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

public class CallbackValidator {

  static final String HEADER_NAME_AUTHORIZATION =
      AuthenticationService.HEADER_KEY_AUTH.toLowerCase(Locale.US);
  static final String HEADER_NAME_SIGNATURE = "x-sinch-webhook-signature";
  static final String HEADER_NAME_ALGORITHM = "x-sinch-webhook-signature-algorithm";
  static final String HEADER_NAME_TIMESTAMP = "x-sinch-webhook-signature-timestamp";
  static final String HEADER_NAME_NONCE = "x-sinch-webhook-signature-nonce";

  private final byte[] secret;

  private Duration duration = Duration.ofMinutes(2);
  private Predicate<String> nonceValidator = s -> true;
  private Predicate<String> oauthValidator = s -> true;

  /** @param secret The secret token defined for the webhook (required) */
  public CallbackValidator(@NonNull final String secret) {
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Duration that the callback is considered valid
   *
   * <p>Default duration is 2min
   *
   * @param duration The duration
   * @return {@link CallbackValidator} this
   */
  public CallbackValidator durationWindow(@NonNull final Duration duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Custom validation of the request nonce
   *
   * <p>Protect against replay attacks by making sure that the nonce is unique. This could be done
   * by keeping track of previously received nonces.
   *
   * <p>This check is ignored if unset
   *
   * @param nonceValidator Predicate receiving the header value and returning whether it is
   *     considered valid
   * @return {@link CallbackValidator} this
   */
  public CallbackValidator nonceValidator(@NonNull final Predicate<String> nonceValidator) {
    this.nonceValidator = nonceValidator;
    return this;
  }

  /**
   * Custom validation of the 'Authorization' header
   *
   * <p>This check is ignored if unset
   *
   * @param oauthValidator Predicate receiving the header value and returning whether it is
   *     considered valid
   * @return {@link CallbackValidator} this
   */
  public CallbackValidator oauth2Validator(@NonNull final Predicate<String> oauthValidator) {
    this.oauthValidator = oauthValidator;
    return this;
  }

  /**
   * Validate the callback (blocking)
   *
   * <p>The callback should be discarded in case of mismatching signatures.
   *
   * @param payload Non-pretty printed JSON string representation of the callback
   * @param headers The HTTP headers of the callback
   * @return Whether the callback is valid
   * @throws Exception If the validation fails
   */
  public boolean validateCallback(final String payload, final Map<String, String> headers) {
    return validateCallbackAsync(payload, headers).join();
  }

  /**
   * Validate the callback
   *
   * <p>The callback should be discarded in case of mismatching signatures.
   *
   * @param payload Non-pretty printed JSON string representation of the callback
   * @param headers The HTTP headers of the callback
   * @return Async task generating whether the callback is valid
   */
  public CompletableFuture<Boolean> validateCallbackAsync(
      @NonNull final String payload, @NonNull final Map<String, String> headers) {
    return mapHeaderValues(headers)
        .thenApply(this::validateValues)
        .thenApply(holder -> generateAndValidateSignature(payload, holder));
  }

  private CompletableFuture<Holder> mapHeaderValues(final Map<String, String> headers) {
    return CompletableFuture.supplyAsync(
        () -> {
          final Map<String, String> headersLowercaseKeys =
              headers.entrySet().stream()
                  .collect(
                      Collectors.toMap(
                          entry -> entry.getKey().toLowerCase(Locale.US), Map.Entry::getValue));
          final Holder holder = new Holder();
          holder.authorization = headersLowercaseKeys.get(HEADER_NAME_AUTHORIZATION);
          holder.signature = headersLowercaseKeys.get(HEADER_NAME_SIGNATURE);
          holder.timestamp = headersLowercaseKeys.get(HEADER_NAME_TIMESTAMP);
          holder.nonce = headersLowercaseKeys.get(HEADER_NAME_NONCE);
          holder.algorithm = headersLowercaseKeys.get(HEADER_NAME_ALGORITHM);
          return holder;
        });
  }

  private Holder validateValues(final Holder holder) {
    if (secret.length == 0) {
      throw Exception.missingSecret();
    }
    if (StringUtils.isEmpty(holder.signature)) {
      throw Exception.missingSignature();
    }
    final boolean notInTime =
        duration.compareTo(
                Duration.between(
                        Instant.ofEpochSecond(Long.parseLong(holder.timestamp)), Instant.now())
                    .abs())
            < 0;
    if (notInTime) {
      throw Exception.notInTime(duration);
    }
    if (!nonceValidator.test(holder.nonce)) {
      throw Exception.failedNonce();
    }
    if (!oauthValidator.test(holder.authorization)) {
      throw Exception.failedOauth();
    }
    return holder;
  }

  @SneakyThrows
  private boolean generateAndValidateSignature(final String payload, final Holder holder) {
    final Mac mac = Mac.getInstance(holder.algorithm);
    mac.init(new SecretKeySpec(secret, holder.algorithm));
    final String generatedSignature =
        Base64.getEncoder()
            .encodeToString(
                mac.doFinal(
                    String.join(".", payload, holder.nonce, holder.timestamp)
                        .getBytes(StandardCharsets.UTF_8)));

    if (!generatedSignature.equals(holder.signature)) {
      throw Exception.signatureMismatch();
    }
    return true;
  }

  private static class Holder {
    String authorization;
    String signature;
    String timestamp;
    String nonce;
    String algorithm;
  }

  public static class Exception extends RuntimeException {

    @Getter private final Reason reason;

    public Exception(final String message, final Reason reason) {
      super(message);
      this.reason = reason;
    }

    private static Exception missingSecret() {
      return new Exception("The hmacSecret is required", Reason.INVALID_SECRET);
    }

    private static Exception missingSignature() {
      return new Exception(
          "Missing required header value: " + HEADER_NAME_SIGNATURE, Reason.MISSING_SIGNATURE);
    }

    private static Exception notInTime(final Duration duration) {
      return new Exception(
          "Timestamp is outside the specified duration: " + duration, Reason.NOT_IN_TIME);
    }

    private static Exception failedNonce() {
      return new Exception("Validation failed for: " + HEADER_NAME_NONCE, Reason.FAILED_NONCE);
    }

    private static Exception failedOauth() {
      return new Exception("Validation failed for: " + HEADER_NAME_SIGNATURE, Reason.FAILED_OAUTH2);
    }

    private static Exception signatureMismatch() {
      return new Exception("The generated signature does not match", Reason.SIGNATURE_MISMATCH);
    }

    public enum Reason {
      INVALID_SECRET,
      MISSING_SIGNATURE,
      NOT_IN_TIME,
      FAILED_NONCE,
      FAILED_OAUTH2,
      SIGNATURE_MISMATCH
    }
  }
}
