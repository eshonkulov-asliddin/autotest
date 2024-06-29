package uz.mu.autotest.exception;

public class AddEntityFailedException extends RuntimeException {
    public AddEntityFailedException() {
    }

    public AddEntityFailedException(String message) {
        super(message);
    }

    public AddEntityFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddEntityFailedException(Throwable cause) {
        super(cause);
    }

    public AddEntityFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
