package agsamkin.code.exception;

import java.io.IOException;

public class GHRateLimitException extends IOException {
    public GHRateLimitException(String message) {
        super(message);
    }

    public GHRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
