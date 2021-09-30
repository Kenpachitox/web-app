package org.example.app.exception;

public class NotCreateSecretCodeException extends RuntimeException {
    public NotCreateSecretCodeException() {
    }

    public NotCreateSecretCodeException(String message) {
        super(message);
    }

    public NotCreateSecretCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotCreateSecretCodeException(Throwable cause) {
        super(cause);
    }

    public NotCreateSecretCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

