package agsamkin.code.exception.github;

import java.io.IOException;

public class GHIssueGettingException extends IOException {

    public GHIssueGettingException(String message) {
        super(message);
    }

    public GHIssueGettingException(String message, Throwable cause) {
        super(message, cause);
    }

}
