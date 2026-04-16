package fr.epita.backend.converter.ControllerConverter;

import fr.epita.backend.controller.api.request.GameRequest;
import fr.epita.backend.controller.api.response.GameResponses.GameResponse;
import fr.epita.backend.controller.api.response.GameResponses.GameVersionResponse;
import fr.epita.backend.controller.api.response.GameResponses.GameVersionsResponse;
import fr.epita.backend.controller.api.response.GameResponses.GamesResponse;
import fr.epita.backend.domain.entity.GameArticleVersionEntity;
import fr.epita.backend.domain.entity.GameEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameControllerConverter {

    public GameEntity fromRequestToEntity(GameRequest request) {
        GameEntity entity = new GameEntity();
        entity.setAuthorId(request.getAuthorId());
        entity.setSubjectGameName(request.getSubjectGameName());
        entity.setArticleName(request.getArticleName());
        entity.setArticleContent(request.getArticleContent());
        return entity;
    }

    public GameResponse fromEntityToResponse(GameEntity entity) {
        GameResponse response = new GameResponse();
        response.setUuid(entity.getUuid());
        response.setAuthorId(entity.getAuthorId());
        response.setAuthorLogin(entity.getAuthorLogin());
        response.setSubjectGameName(entity.getSubjectGameName());
        response.setArticleName(entity.getArticleName());
        response.setArticleContent(entity.getArticleContent());
        response.setVersion(entity.getVersion());
        response.setCreatedAt(entity.getCreatedAt());
        response.setStatus(entity.getStatus());
        return response;
    }

    public GamesResponse fromEntitiesToResponses(List<GameEntity> entities) {
        List<GameResponse> responses = new ArrayList<>();

        for (GameEntity entity : entities) {
            responses.add(fromEntityToResponse(entity));
        }

        GamesResponse wrapper = new GamesResponse();
        wrapper.setList(responses);
        return wrapper;
    }

    public GameVersionResponse fromVersionEntityToResponse(GameArticleVersionEntity entity) {
        GameVersionResponse response = new GameVersionResponse();
        response.setUuid(entity.getUuid());
        response.setGameId(entity.getGameId());
        response.setAuthorId(entity.getAuthorId());
        response.setAuthorLogin(entity.getAuthorLogin());
        response.setArticleName(entity.getArticleName());
        response.setArticleContent(entity.getArticleContent());
        response.setVersion(entity.getVersion());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    public GameVersionsResponse fromVersionEntitiesToResponses(List<GameArticleVersionEntity> entities) {
        List<GameVersionResponse> responses = new ArrayList<>();

        for (GameArticleVersionEntity entity : entities) {
            responses.add(fromVersionEntityToResponse(entity));
        }

        GameVersionsResponse wrapper = new GameVersionsResponse();
        wrapper.setList(responses);
        return wrapper;
    }
}
