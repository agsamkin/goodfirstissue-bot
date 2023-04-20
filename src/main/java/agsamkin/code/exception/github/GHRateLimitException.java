package agsamkin.code.exception.github;

import java.io.IOException;

public class GHRateLimitException extends IOException {
    public GHRateLimitException(String message) {
        super(message);
    }

    public GHRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
