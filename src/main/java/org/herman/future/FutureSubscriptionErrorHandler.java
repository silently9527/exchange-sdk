package org.herman.future;


import org.herman.exception.ApiException;

/**
 * The error handler for the subscription.
 */
@FunctionalInterface
public interface FutureSubscriptionErrorHandler {

  void onError(ApiException exception);
}
