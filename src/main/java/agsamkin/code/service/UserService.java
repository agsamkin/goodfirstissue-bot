package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User getUserByUserId(Long userId);

    User setLanguage(Long userId, Language language);
    User removeLanguage(Long userId, Language language);
    List<Language> getUserLanguages(Long userId);
}
