package agsamkin.code.service;

import agsamkin.code.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface UserService {
    User registerUser(Message msg);
}
