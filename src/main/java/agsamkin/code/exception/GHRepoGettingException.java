package agsamkin.code.exception;

import java.io.IOException;

public class GHRepoGettingException extends IOException {
    public GHRepoGettingException(String message) {
        super(message);
    }

    public GHRepoGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
