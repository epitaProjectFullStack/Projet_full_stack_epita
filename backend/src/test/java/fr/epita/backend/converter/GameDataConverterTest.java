package fr.epita.backend.converter;

import fr.epita.backend.converter.DataConverter.GameDataConverter;
import fr.epita.backend.data.model.*;
import fr.epita.backend.domain.entity.*;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

class GameDataConverterTest {

    private final GameDataConverter converter = new GameDataConverter();

    @Test
    void fromModelToEntity_should_map_basic_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // GameModel is converted to GameEntity
        // ============================================================

        GameModel model = new GameModel();
        model.setSubjectGameName("Zelda");

        GameEntity entity = converter.fromModelToEntity(model);

        assertThat(entity.getSubjectGameName()).isEqualTo("Zelda");
    }

   @Test
    void fromVersionModelToEntity_should_map_fields() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Version model is correctly converted with nested objects
        // ============================================================

        // WHY:
        // Le converter dépend de game + author → ils doivent être non null

        UUID gameId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        GameModel game = new GameModel();
        game.setUuid(gameId);

        UserModel user = new UserModel();
        user.setUuid(authorId);
        user.setLogin("alice");

        GameArticleVersionModel model = new GameArticleVersionModel();
        model.setGame(game);
        model.setAuthor(user);
        model.setVersionNumber(3);

        // HOW:
        // On injecte toutes les dépendances nécessaires

        GameArticleVersionEntity entity =
                converter.fromVersionModelToEntity(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getVersion()).isEqualTo(3);
        assertThat(entity.getGameId()).isEqualTo(gameId);
        assertThat(entity.getAuthorId()).isEqualTo(authorId);
    }
}