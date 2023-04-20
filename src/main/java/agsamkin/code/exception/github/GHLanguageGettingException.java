package agsamkin.code.exception.github;

public class GHLanguageGettingException extends RuntimeException {
    public GHLanguageGettingException(String message) {
        super(message);
    }

    public GHLanguageGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
