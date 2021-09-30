package org.example.app.exception;

public class CreateOrderException extends RuntimeException {
    public CreateOrderException() {
    }

    public CreateOrderException(String message) {
        super(message);
    }

    public CreateOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateOrderException(Throwable cause) {
        super(cause);
    }

    public CreateOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

