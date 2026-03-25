package fr.epita.backend.data.repository;

import fr.epita.backend.data.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByLogin(String login);

    Optional<UserModel> findByMail(String mail);

}
