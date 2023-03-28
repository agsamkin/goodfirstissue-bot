package agsamkin.code.service.impl;

import agsamkin.code.model.User;
import agsamkin.code.repository.UserRepository;
import agsamkin.code.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User registerUser(Message msg) {
        User user = userRepository.findById(msg.getFrom().getId())
                .map(u -> {
                    u.setUserName(msg.getFrom().getUserName());
                    u.setLanguageCode(msg.getFrom().getLanguageCode());
                    return u;
                }).orElse(
                        User.builder()
                            .userId(msg.getFrom().getId())
                            .userName(msg.getFrom().getUserName())
                            .languageCode(msg.getFrom().getLanguageCode()).build()
                );
        return userRepository.save(user);
    }
}
