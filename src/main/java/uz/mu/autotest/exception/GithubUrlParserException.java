package uz.mu.autotest.exception;

public class GithubUrlParserException extends RuntimeException {
    public GithubUrlParserException() {
    }

    public GithubUrlParserException(String message) {
        super(message);
    }

    public GithubUrlParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public GithubUrlParserException(Throwable cause) {
        super(cause);
    }

    public GithubUrlParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
