package com.sinch.sdk.api.conversationapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.sinch.sdk.api.conversationapi.ConversationApiConfig;
import com.sinch.sdk.exception.ApiException;
import com.sinch.sdk.restclient.SinchRestClient;
import com.sinch.sdk.util.ExceptionUtils;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BaseServiceTest {

  protected static final String BASE_URL = "http://localhost";
  protected static final String PROJECT_ID = "project-id";

  protected static final String EXPECTED_SERVICE_URI_FORMAT =
      String.format(
          AbstractService.TEMPLATE_URL, BASE_URL, AbstractService.API_VERSION, PROJECT_ID, "%s");
  protected static final ConversationApiConfig CONFIG =
      ConversationApiConfig.builder().baseUrl(BASE_URL).projectId(PROJECT_ID).build();

  private static final CompletableFuture<?> failedFuture = new CompletableFuture<>();

  static {
    failedFuture.completeExceptionally(new ApiException(0, ""));
  }

  @Spy protected RestClientStub restClient;

  protected URI uriPathEndsWithMatcher(final String suffix) {
    return argThat(uri -> uri.getPath().endsWith(suffix));
  }

  protected URI uriQueryEndsWithMatcher(final String suffix) {
    return argThat(uri -> uri.getQuery().endsWith(suffix));
  }

  protected void givenGetThrows() {
    lenient().doReturn(failedFuture).when(restClient).get(any(URI.class), any(), anyMap());
  }

  protected void verifyPostVoid(final Supplier<URI> uriMatcher) {
    verify(restClient).post(uriMatcher.get(), anyMap());
  }

  protected void verifyPostVoid(final Supplier<URI> uriMatcher, final Supplier<?> requestMatcher) {
    verify(restClient).post(uriMatcher.get(), requestMatcher.get(), anyMap());
  }

  protected void verifyPostCalled(final Supplier<URI> uriMatcher, final Class<?> clazz) {
    verifyPostCalled(uriMatcher, clazz, () -> isA(clazz));
  }

  protected void verifyPostCalled(
      final Supplier<URI> uriMatcher,
      final Class<?> responseClass,
      final Supplier<?> requestMatcher) {
    verify(restClient).post(uriMatcher.get(), eq(responseClass), requestMatcher.get(), anyMap());
  }

  protected void verifyDeleteCalled(final Supplier<URI> uriMatcher) {
    verify(restClient).delete(uriMatcher.get(), anyMap());
  }

  protected void verifyGetCalled(final Supplier<URI> uriMatcher, final Class<?> clazz) {
    verify(restClient).get(uriMatcher.get(), eq(clazz), anyMap());
  }

  protected void verifyPatchCalled(final Supplier<URI> uriMatcher, final Class<?> clazz) {
    verify(restClient).patch(uriMatcher.get(), eq(clazz), isA(clazz), anyMap());
  }

  protected ApiException verifyThrowsApiException(final ThrowingCallable throwingCallable) {
    final Throwable throwable = catchThrowable(throwingCallable);
    assertThat(throwable).isInstanceOf(ApiException.class);
    return (ApiException) throwable;
  }

  protected void assertClientSideException(final ThrowingCallable throwingCallable) {
    final ApiException apiException = verifyThrowsApiException(throwingCallable);
    assertThat(apiException.getCode()).isEqualTo(ExceptionUtils.BAD_REQUEST);
    assertThat(apiException.getResponseHeaders()).isNull();
    assertThat(apiException.getResponseBody()).isNull();
  }

  static class RestClientStub implements SinchRestClient {

    @Override
    public <T> CompletableFuture<T> get(URI uri, Class<T> clazz, Map<String, String> headers) {
      return CompletableFuture.completedFuture(mock(clazz));
    }

    @Override
    public CompletableFuture<Void> post(URI uri, Map<String, String> headers) {
      return CompletableFuture.completedFuture(null).thenAccept(o -> {});
    }

    @Override
    public <S> CompletableFuture<Void> post(URI uri, S body, Map<String, String> headers) {
      return CompletableFuture.completedFuture(null).thenAccept(o -> {});
    }

    @Override
    public <T, S> CompletableFuture<T> post(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return CompletableFuture.completedFuture(mock(clazz));
    }

    @Override
    public <T, S> CompletableFuture<T> patch(
        URI uri, Class<T> clazz, S body, Map<String, String> headers) {
      return CompletableFuture.completedFuture(mock(clazz));
    }

    @Override
    public CompletableFuture<Void> delete(URI uri, Map<String, String> headers) {
      return CompletableFuture.completedFuture(null).thenAccept(o -> {});
    }
  }
}
