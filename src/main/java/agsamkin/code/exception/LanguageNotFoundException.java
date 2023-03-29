package agsamkin.code.exception;

import javax.persistence.EntityNotFoundException;

public class LanguageNotFoundException extends EntityNotFoundException {
    public LanguageNotFoundException(String msg) {
        super(msg);
    }
}
