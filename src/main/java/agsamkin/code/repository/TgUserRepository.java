package agsamkin.code.repository;

import agsamkin.code.model.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TgUserRepository extends JpaRepository<TgUser, Long> {
    TgUser findByUserId(Long userId);
}
