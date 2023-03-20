package agsamkin.code.service.impl;

import agsamkin.code.model.TgUser;
import agsamkin.code.repository.TgUserRepository;
import agsamkin.code.service.TgUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Objects;


@RequiredArgsConstructor
@Service
public class TgUserServiceImpl implements TgUserService {

    private final TgUserRepository tgUserRepository;

    @Override
    public TgUser getTgUserByUserId(Long userId) {
        return tgUserRepository.findByUserId(userId);
    }

    @Override
    public TgUser registerTgUser(User from) {
        TgUser tgUser = getTgUserByUserId(from.getId());
        if (Objects.isNull(tgUser)) {
            tgUser = new TgUser();
            tgUser.setUserId(from.getId());
        }
        tgUser.setFirstName(from.getFirstName());
        tgUser.setLastName(from.getLastName());
        tgUser.setUserName(from.getUserName());
        tgUser.setLanguageCode(from.getLanguageCode());
        return tgUserRepository.save(tgUser);
    }
}
