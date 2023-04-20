package agsamkin.code.service.impl;

import agsamkin.code.model.User;
import agsamkin.code.model.setting.Setting;
import agsamkin.code.repository.SettingRepository;
import agsamkin.code.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Setting> getUserSetting(User user) {
        return settingRepository.findByUser(user);
    }

    @Override
    public Setting setUserSetting(Setting setting) {
        return settingRepository.save(
                getUserSetting(setting.getUser())
                        .map(existingSetting -> {
                            existingSetting.setRepoSort(setting.getRepoSort());
                            existingSetting.setRepoOrder(setting.getRepoOrder());
                            return existingSetting;
                        })
                        .orElse(setting));
    }
}
