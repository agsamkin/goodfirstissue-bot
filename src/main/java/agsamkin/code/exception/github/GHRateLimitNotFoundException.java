package agsamkin.code.exception.github;

public class GHRateLimitNotFoundException extends RuntimeException {
    public GHRateLimitNotFoundException(String message) {
        super(message);
    }

    public GHRateLimitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
