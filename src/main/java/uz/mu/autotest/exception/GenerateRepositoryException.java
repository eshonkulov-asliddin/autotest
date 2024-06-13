package uz.mu.autotest.exception;

public class GenerateRepositoryException extends RuntimeException {
    public GenerateRepositoryException() {
    }

    public GenerateRepositoryException(String message) {
        super(message);
    }

    public GenerateRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerateRepositoryException(Throwable cause) {
        super(cause);
    }

    public GenerateRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
