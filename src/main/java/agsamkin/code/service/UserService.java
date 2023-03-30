package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User getUserById(long userId);

    User setLanguage(long userId, Language language);
    User removeLanguage(long userId, Language language);
    List<Language> getUserLanguages(Long userId);
}
