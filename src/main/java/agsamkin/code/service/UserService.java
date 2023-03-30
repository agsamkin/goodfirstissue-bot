package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User getUserById(long userId);

    User setLanguage(long userId, Language language);
    User removeLanguage(long userId, Language language);
    List<Language> getLanguagesByUserId(Long userId);
}
