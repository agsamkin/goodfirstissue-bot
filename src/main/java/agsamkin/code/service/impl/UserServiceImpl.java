package agsamkin.code.service.impl;

import agsamkin.code.exception.UserNotFoundException;
import agsamkin.code.model.Language;
import agsamkin.code.model.User;
import agsamkin.code.repository.UserRepository;
import agsamkin.code.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        User existingUser = userRepository.findByUserId(user.getUserId())
                .map(u -> {
                    u.setUserName(user.getUserName());
                    u.setLanguageCode(user.getLanguageCode());
                    return u;
                }).orElse(user);
        return userRepository.save(existingUser);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User setLanguage(long userId, Language language) {
        User user = getUserById(userId);
        if (user.getLanguages().contains(language)) {
            return user;
        }
        user.setLanguage(language);
        return userRepository.save(user);
    }

    @Override
    public User removeLanguage(long userId, Language language) {
        User user = getUserById(userId);
        if (!user.getLanguages().contains(language)) {
            return user;
        }
        user.removeLanguage(language);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Language> getUserLanguages(Long userId) {
        User user = getUserById(userId);
        return user.getLanguages();
    }
}
