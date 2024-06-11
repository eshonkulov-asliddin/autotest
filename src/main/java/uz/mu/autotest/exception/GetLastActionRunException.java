package uz.mu.autotest.exception;

public class GetLastActionRunException extends RuntimeException {
    public GetLastActionRunException() {
    }

    public GetLastActionRunException(String message) {
        super(message);
    }

    public GetLastActionRunException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetLastActionRunException(Throwable cause) {
        super(cause);
    }

    public GetLastActionRunException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
