package agsamkin.code.service;

import agsamkin.code.model.User;
import agsamkin.code.model.setting.Setting;

import java.util.Optional;

public interface SettingService {
    Optional<Setting> getUserSetting(User user);
    Setting setUserSetting(Setting setting);
}
