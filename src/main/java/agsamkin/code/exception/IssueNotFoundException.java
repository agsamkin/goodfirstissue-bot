package agsamkin.code.exception;

import javax.persistence.EntityNotFoundException;

public class IssueNotFoundException extends EntityNotFoundException {
    public IssueNotFoundException(String message) {
        super(message);
    }
}
