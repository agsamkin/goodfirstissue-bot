package agsamkin.code.exception;

import javax.persistence.EntityNotFoundException;

public class VisibilityNotFoundException extends EntityNotFoundException {
    public VisibilityNotFoundException(String message) {
        super(message);
    }
}
