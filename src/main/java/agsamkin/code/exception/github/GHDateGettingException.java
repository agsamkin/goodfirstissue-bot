package agsamkin.code.exception.github;

import java.io.IOException;

public class GHDateGettingException extends IOException {
    public GHDateGettingException(String message) {
        super(message);
    }

    public GHDateGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
