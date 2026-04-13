package fr.epita.backend.service;

import fr.epita.backend.domain.service.GameService;
import fr.epita.backend.data.repository.*;
import fr.epita.backend.converter.DataConverter.GameDataConverter;
import fr.epita.backend.domain.entity.GameEntity;
import fr.epita.backend.domain.entity.GameArticleVersionEntity;
import fr.epita.backend.data.model.GameModel;
import fr.epita.backend.data.model.GameArticleVersionModel;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.domain.service.KafkaProducerService;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private final GameRepository gameRepository = mock(GameRepository.class);
    private final GameArticleVersionRepository versionRepository = mock(GameArticleVersionRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final GameDataConverter converter = mock(GameDataConverter.class);
    private final KafkaProducerService kafka = mock(KafkaProducerService.class);

    private final GameService service = new GameService(gameRepository, versionRepository, userRepository, converter,
            kafka);

    @Test
    void createGame_should_work() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating a valid game triggers persistence + Kafka event
        // ============================================================

        // HOW:
        // Cas nominal → toutes les données sont valides

        UUID userId = UUID.randomUUID();

        GameEntity entity = new GameEntity();
        entity.setAuthorId(userId);
        entity.setSubjectGameName("Zelda");
        entity.setArticleName("article");
        entity.setArticleContent("content");

        UserModel user = new UserModel();
        user.setBanned(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findBySubjectGameName("Zelda")).thenReturn(Optional.empty());

        // simulation du save JPA
        when(gameRepository.save(any())).thenAnswer(invocation -> {
            GameModel m = invocation.getArgument(0);
            m.setUuid(UUID.randomUUID());
            return m;
        });

        when(converter.fromModelToEntity(any())).thenReturn(new GameEntity());

        // HOW:
        // -appel réel du service
        // -puis on verifie les interactions

        service.createGame(entity);

        verify(gameRepository, atLeastOnce()).save(any());
        verify(versionRepository).save(any());
        verify(kafka).sendGameCreated(any());
    }

    @Test
    void createGame_should_fail_if_user_not_found() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating a game fails if author does not exist
        // ============================================================

        // WHY:
        // sécurité → auteur obligatoire

        UUID userId = UUID.randomUUID();

        GameEntity entity = new GameEntity();
        entity.setAuthorId(userId);
        entity.setSubjectGameName("Zelda");
        entity.setArticleName("a");
        entity.setArticleContent("b");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // HOW:
        // appel → exception attendue

        assertThatThrownBy(() -> service.createGame(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createGame_should_fail_if_name_exists() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating a game fails if name already exists
        // ============================================================

        UUID userId = UUID.randomUUID();

        GameEntity entity = new GameEntity();
        entity.setAuthorId(userId);
        entity.setSubjectGameName("Zelda");
        entity.setArticleName("a");
        entity.setArticleContent("b");

        UserModel user = new UserModel();
        user.setBanned(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findBySubjectGameName("Zelda"))
                .thenReturn(Optional.of(mock(GameModel.class)));

        assertThatThrownBy(() -> service.createGame(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createGame_should_fail_if_invalid_entity() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating a game fails if entity is invalid (missing fields)
        // ============================================================

        GameEntity entity = new GameEntity(); // vide

        // HOW:
        // validation interne → exception

        assertThatThrownBy(() -> service.createGame(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getGame_should_return_entity() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving an existing game returns entity
        // ============================================================

        UUID id = UUID.randomUUID();

        GameModel model = new GameModel();

        when(gameRepository.findById(id)).thenReturn(Optional.of(model));
        when(converter.fromModelToEntity(model)).thenReturn(new GameEntity());

        GameEntity result = service.getGame(id);

        assertThat(result).isNotNull();
    }

    @Test
    void getGame_should_fail_if_not_found() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving unknown game throws exception
        // ============================================================

        UUID id = UUID.randomUUID();

        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getGame(id))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getGames_should_return_list() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving all games returns a list
        // ============================================================

        when(gameRepository.findAll()).thenReturn(List.of(new GameModel()));
        when(converter.fromModelToEntity(any())).thenReturn(new GameEntity());

        List<GameEntity> result = service.getGames();

        assertThat(result).isNotEmpty();
    }

    @Test
    void getGameVersions_should_return_versions() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving versions returns list of versions
        // ============================================================

        UUID id = UUID.randomUUID();

        when(gameRepository.findById(id)).thenReturn(Optional.of(new GameModel()));
        when(versionRepository.findByGameUuidOrderByVersionNumberDesc(id))
                .thenReturn(List.of(new GameArticleVersionModel()));

        when(converter.fromVersionModelToEntity(any()))
                .thenReturn(new GameArticleVersionEntity());

        List<GameArticleVersionEntity> result = service.getGameVersions(id);

        assertThat(result).isNotEmpty();
    }

    @Test
    void deleteGame_should_delete() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Deleting a game calls repository delete
        // ============================================================

        UUID id = UUID.randomUUID();

        GameModel model = new GameModel();

        when(gameRepository.findById(id)).thenReturn(Optional.of(model));

        service.deleteGame(id);

        verify(gameRepository).delete(model);
    }

    @Test
    void deleteGame_should_fail_if_not_found() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Deleting unknown game throws exception
        // ============================================================

        UUID id = UUID.randomUUID();

        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteGame(id))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateGame_should_update_game() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating a game creates a new version and saves it
        // ============================================================

        // WHY:
        // Cas métier principal → modification d’un jeu existant

        UUID gameId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        GameEntity entity = new GameEntity();
        entity.setAuthorId(userId);
        entity.setSubjectGameName("Zelda");
        entity.setArticleName("updated");
        entity.setArticleContent("content");

        GameModel gameModel = new GameModel();
        gameModel.setSubjectGameName("Zelda");

        UserModel user = new UserModel();
        user.setBanned(false);

        // simulation current version
        GameArticleVersionModel currentVersion = new GameArticleVersionModel();
        currentVersion.setVersionNumber(1);
        gameModel.setCurrentVersion(currentVersion);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameModel));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(converter.fromModelToEntity(any())).thenReturn(new GameEntity());

        // HOW: appel du service → on vérifie sauvegarde version

        service.updateGame(gameId, entity);

        verify(versionRepository).save(any());
        verify(gameRepository, atLeastOnce()).save(any());
    }

    @Test
    void updateGame_should_change_name_if_different() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating with a different name triggers name validation
        // ============================================================

        UUID gameId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        GameEntity entity = new GameEntity();
        entity.setAuthorId(userId);
        entity.setSubjectGameName("NEW_NAME");
        entity.setArticleName("a");
        entity.setArticleContent("b");

        GameModel gameModel = new GameModel();
        gameModel.setSubjectGameName("OLD_NAME");

        UserModel user = new UserModel();
        user.setBanned(false);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameModel));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findBySubjectGameName("NEW_NAME")).thenReturn(Optional.empty());
        when(converter.fromModelToEntity(any())).thenReturn(new GameEntity());

        service.updateGame(gameId, entity);

        verify(gameRepository).findBySubjectGameName("NEW_NAME");
    }

    @Test
    void updateGame_should_fail_if_game_not_found() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating a non-existing game throws exception
        // ============================================================

        UUID gameId = UUID.randomUUID();

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateGame(gameId, new GameEntity()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void revertGame_should_create_new_version_from_old() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Reverting a game creates a new version from previous one
        // ============================================================

        UUID gameId = UUID.randomUUID();
        UUID versionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        GameModel gameModel = new GameModel();
        gameModel.setSubjectGameName("Zelda");

        GameArticleVersionModel version = new GameArticleVersionModel();
        version.setArticleName("old");
        version.setArticleContent("old content");

        UserModel user = new UserModel();
        user.setBanned(false);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameModel));
        when(versionRepository.findByUuidAndGameUuid(versionId, gameId))
                .thenReturn(Optional.of(version));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(converter.fromModelToEntity(any())).thenReturn(new GameEntity());

        service.revertGame(gameId, versionId, userId);

        verify(versionRepository).save(any());
        verify(gameRepository, atLeastOnce()).save(any());
    }

    @Test
    void revertGame_should_fail_if_version_not_found() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Reverting fails if version does not exist
        // ============================================================

        UUID gameId = UUID.randomUUID();
        UUID versionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(new GameModel()));
        when(versionRepository.findByUuidAndGameUuid(versionId, gameId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.revertGame(gameId, versionId, userId))
                .isInstanceOf(RuntimeException.class);
    }
}
