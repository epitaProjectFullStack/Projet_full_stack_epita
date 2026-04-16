package fr.epita.backend.converter.DataConverter;

import fr.epita.backend.data.model.GameArticleVersionModel;
import fr.epita.backend.data.model.GameModel;
import fr.epita.backend.domain.entity.GameArticleVersionEntity;
import fr.epita.backend.domain.entity.GameEntity;
import org.springframework.stereotype.Component;

@Component
public class GameDataConverter {

    public GameEntity fromModelToEntity(GameModel model) {
        GameArticleVersionModel currentVersion = model.getCurrentVersion();

        GameEntity entity = new GameEntity();
        entity.setUuid(model.getUuid());
        entity.setSubjectGameName(model.getSubjectGameName());
        entity.setStatus(model.getStatus());

        if (currentVersion != null) {
            entity.setAuthorId(currentVersion.getAuthor().getUuid());
            entity.setAuthorLogin(currentVersion.getAuthor().getLogin());
            entity.setArticleName(currentVersion.getArticleName());
            entity.setArticleContent(currentVersion.getArticleContent());
            entity.setVersion(currentVersion.getVersionNumber());
            entity.setCreatedAt(currentVersion.getCreatedAt());
        }

        return entity;
    }

    public GameArticleVersionEntity fromVersionModelToEntity(GameArticleVersionModel model) {
        GameArticleVersionEntity entity = new GameArticleVersionEntity();
        entity.setUuid(model.getUuid());
        entity.setGameId(model.getGame().getUuid());
        entity.setAuthorId(model.getAuthor().getUuid());
        entity.setAuthorLogin(model.getAuthor().getLogin());
        entity.setArticleName(model.getArticleName());
        entity.setArticleContent(model.getArticleContent());
        entity.setVersion(model.getVersionNumber());
        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }
}
