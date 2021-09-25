package org.example.app.exception;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException() {
    }

    public NotEnoughFundsException(String message) {
        super(message);
    }

    public NotEnoughFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughFundsException(Throwable cause) {
        super(cause);
    }

    public NotEnoughFundsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

