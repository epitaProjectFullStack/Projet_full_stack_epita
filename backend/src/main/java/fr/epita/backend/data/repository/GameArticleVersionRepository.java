package fr.epita.backend.data.repository;

import fr.epita.backend.data.model.GameArticleVersionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameArticleVersionRepository extends JpaRepository<GameArticleVersionModel, UUID> {
    List<GameArticleVersionModel> findByGameUuidOrderByVersionNumberDesc(UUID gameId);

    Optional<GameArticleVersionModel> findByUuidAndGameUuid(UUID versionId, UUID gameId);
}
