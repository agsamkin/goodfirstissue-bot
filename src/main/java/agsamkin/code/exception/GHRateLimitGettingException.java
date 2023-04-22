package agsamkin.code.exception;

import java.io.IOException;

public class GHRateLimitGettingException extends IOException {
    public GHRateLimitGettingException(String message) {
        super(message);
    }

    public GHRateLimitGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
