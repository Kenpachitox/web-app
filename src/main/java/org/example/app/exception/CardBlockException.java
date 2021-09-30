package org.example.app.exception;

public class CardBlockException extends RuntimeException {
  public CardBlockException() {
  }

  public CardBlockException(String message) {
    super(message);
  }

  public CardBlockException(String message, Throwable cause) {
    super(message, cause);
  }

  public CardBlockException(Throwable cause) {
    super(cause);
  }

  public CardBlockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
