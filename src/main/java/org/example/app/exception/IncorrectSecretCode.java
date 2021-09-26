package org.example.app.exception;

public class IncorrectSecretCode extends RuntimeException {
    public IncorrectSecretCode() {
    }

    public IncorrectSecretCode(String message) {
        super(message);
    }

    public IncorrectSecretCode(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectSecretCode(Throwable cause) {
        super(cause);
    }

    public IncorrectSecretCode(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

