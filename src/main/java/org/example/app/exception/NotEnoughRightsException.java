package org.example.app.exception;

public class NotEnoughRightsException extends RuntimeException {
    public NotEnoughRightsException() {
    }

    public NotEnoughRightsException(String message) {
        super(message);
    }

    public NotEnoughRightsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughRightsException(Throwable cause) {
        super(cause);
    }

    public NotEnoughRightsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

