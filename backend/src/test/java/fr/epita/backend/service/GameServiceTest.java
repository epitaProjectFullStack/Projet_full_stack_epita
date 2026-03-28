package fr.epita.backend.service;

import fr.epita.backend.domain.service.GameService;
import fr.epita.backend.data.repository.*;
import fr.epita.backend.converter.DataConverter.GameDataConverter;
import fr.epita.backend.domain.entity.GameEntity;
import fr.epita.backend.data.model.GameModel;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.domain.service.KafkaProducerService;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private final GameRepository gameRepository = mock(GameRepository.class);
    private final GameArticleVersionRepository versionRepository = mock(GameArticleVersionRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final GameDataConverter converter = mock(GameDataConverter.class);
    private final KafkaProducerService kafka = mock(KafkaProducerService.class);

    private final GameService service =
            new GameService(gameRepository, versionRepository, userRepository, converter, kafka);

    @Test
    void createGame_should_work() {
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

        when(gameRepository.save(any())).thenAnswer(invocation -> {
            GameModel m = invocation.getArgument(0);
            m.setUuid(UUID.randomUUID()); // simule JPA
            return m;
        });

        service.createGame(entity);

        verify(gameRepository, atLeastOnce()).save(any());
        verify(versionRepository).save(any());
        verify(kafka).sendGameCreated(any());
    }

    @Test
    void createGame_should_fail_if_user_not_found() {
        UUID userId = UUID.randomUUID();

        GameEntity entity = new GameEntity();
        entity.setAuthorId(userId);
        entity.setSubjectGameName("Zelda");
        entity.setArticleName("a");
        entity.setArticleContent("b");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createGame(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createGame_should_fail_if_name_exists() {
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
}