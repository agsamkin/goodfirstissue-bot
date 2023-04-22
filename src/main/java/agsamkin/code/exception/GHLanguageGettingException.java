package agsamkin.code.exception;

public class GHLanguageGettingException extends RuntimeException {
    public GHLanguageGettingException(String message) {
        super(message);
    }

    public GHLanguageGettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
