package agsamkin.code.exception.github;

import java.io.IOException;

public class GHRepoLanguagesGettingException extends IOException {
    public GHRepoLanguagesGettingException(String message) {
        super(message);
    }

    public GHRepoLanguagesGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
