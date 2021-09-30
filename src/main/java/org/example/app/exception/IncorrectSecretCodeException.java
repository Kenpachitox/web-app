package org.example.app.exception;

public class IncorrectSecretCodeException extends RuntimeException {
    public IncorrectSecretCodeException() {
    }

    public IncorrectSecretCodeException(String message) {
        super(message);
    }

    public IncorrectSecretCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectSecretCodeException(Throwable cause) {
        super(cause);
    }

    public IncorrectSecretCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

