package org.example.app.exception;

public class PasswordResetException extends RuntimeException {
    public PasswordResetException() {
    }

    public PasswordResetException(String message) {
        super(message);
    }

    public PasswordResetException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordResetException(Throwable cause) {
        super(cause);
    }

    public PasswordResetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

