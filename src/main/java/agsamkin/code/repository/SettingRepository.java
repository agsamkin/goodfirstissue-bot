package agsamkin.code.repository;

import agsamkin.code.model.User;
import agsamkin.code.model.setting.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByUser(User user);
}
