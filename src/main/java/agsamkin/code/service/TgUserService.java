package agsamkin.code.service;

import agsamkin.code.model.TgUser;
import org.telegram.telegrambots.meta.api.objects.User;

public interface TgUserService {
    TgUser getTgUserByUserId(Long userId);
    TgUser registerTgUser(User from);
}
