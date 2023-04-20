package agsamkin.code.exception.github;

import java.io.IOException;

public class GHRepoGettingException extends IOException {
    public GHRepoGettingException(String message) {
        super(message);
    }

    public GHRepoGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
