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
        // Version model is correctly converted including nested objects
        // ============================================================

        // WHY:
        // Le converter dépend de game + author → ils doivent être présents

        GameModel game = new GameModel();
        game.setUuid(UUID.randomUUID());

        UserModel user = new UserModel();
        user.setUuid(UUID.randomUUID());
        user.setLogin("alice");

        GameArticleVersionModel model = new GameArticleVersionModel();
        model.setGame(game);
        model.setAuthor(user);

        GameArticleVersionEntity entity =
                converter.fromVersionModelToEntity(model);

        assertThat(entity.getVersion()).isEqualTo(3);
        assertThat(entity.getGameId()).isEqualTo(game.getUuid());
        assertThat(entity.getAuthorId()).isEqualTo(user.getUuid());
    }
}