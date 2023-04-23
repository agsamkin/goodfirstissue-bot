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
        return userRepository.save(
                userRepository.findByUserId(user.getUserId())
                .map(existingUser -> {
                    existingUser.setUserName(user.getUserName());
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setLanguageCode(user.getLanguageCode());
                    return existingUser;
                }).orElse(user)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByUserId(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
    }

    @Override
    public User setLanguage(Long userId, Language language) {
        User user = getUserByUserId(userId);
        if (user.getLanguages().contains(language)) {
            return user;
        }
        user.setLanguage(language);
        return userRepository.save(user);
    }

    @Override
    public User removeLanguage(Long userId, Language language) {
        User user = getUserByUserId(userId);
        if (!user.getLanguages().contains(language)) {
            return user;
        }
        user.removeLanguage(language);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Language> getUserLanguages(Long userId) {
        User user = getUserByUserId(userId);
        return user.getLanguages();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
