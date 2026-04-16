package fr.epita.backend.data.repository;

import fr.epita.backend.data.model.GameModel;
import fr.epita.backend.utils.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<GameModel, UUID> {
    Optional<GameModel> findBySubjectGameName(String subjectGameName);
    List<GameModel> findAllByStatus(GameStatus status);
}
