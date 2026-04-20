package fr.epita.backend.data.repository;

import fr.epita.backend.data.model.RefreshTokenModel;
import fr.epita.backend.data.model.UserModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {

    Optional<RefreshTokenModel> findByTokenHash(String tokenHash);

    void deleteAllByUser(UserModel user);
}
