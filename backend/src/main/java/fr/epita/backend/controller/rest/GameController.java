package fr.epita.backend.controller.rest;

import fr.epita.backend.controller.api.request.GameModerationRequest;
import fr.epita.backend.controller.api.request.GameRequest;
import fr.epita.backend.controller.api.request.GameRevertRequest;
import fr.epita.backend.controller.api.response.GameResponses.GameResponse;
import fr.epita.backend.controller.api.response.GameResponses.GameVersionsResponse;
import fr.epita.backend.controller.api.response.GameResponses.GamesResponse;
import fr.epita.backend.converter.ControllerConverter.GameControllerConverter;
import fr.epita.backend.domain.entity.GameArticleVersionEntity;
import fr.epita.backend.domain.entity.GameEntity;
import fr.epita.backend.domain.service.GameService;
import fr.epita.backend.utils.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final GameControllerConverter gameControllerConverter;

    public GameController(GameService gameService, GameControllerConverter gameControllerConverter) {
        this.gameService = gameService;
        this.gameControllerConverter = gameControllerConverter;
    }

    @GetMapping
    public ResponseEntity<GamesResponse> getGames() {
        List<GameEntity> games = gameService.getGames();
        return ResponseEntity.ok(gameControllerConverter.fromEntitiesToResponses(games));
    }

    @GetMapping("/review")
    public ResponseEntity<GamesResponse> getGamesToReview() {
        List<GameEntity> games = gameService.getGamesToReview();
        return ResponseEntity.ok(gameControllerConverter.fromEntitiesToResponses(games));
    }

    @GetMapping("/all")
    public ResponseEntity<GamesResponse> getAllGames() {
        List<GameEntity> games = gameService.getAllGames();
        return ResponseEntity.ok(gameControllerConverter.fromEntitiesToResponses(games));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GameResponse> moderateGame(@PathVariable UUID id, @RequestBody GameModerationRequest request) {
        if (request == null || request.getStatus() == null)
            ErrorCode.INVALID_REQUEST.throwException();

        GameEntity moderatedGame = gameService.moderateGame(id, request.getStatus());
        return ResponseEntity.ok(gameControllerConverter.fromEntityToResponse(moderatedGame));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGame(@PathVariable UUID id) {
        GameEntity game = gameService.getGame(id);
        return ResponseEntity.ok(gameControllerConverter.fromEntityToResponse(game));
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<GameVersionsResponse> getVersions(@PathVariable UUID id) {
        List<GameArticleVersionEntity> versions = gameService.getGameVersions(id);
        return ResponseEntity.ok(gameControllerConverter.fromVersionEntitiesToResponses(versions));
    }

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@RequestBody GameRequest request) {
        if (request == null)
            ErrorCode.INVALID_REQUEST.throwException();

        GameEntity entity = gameControllerConverter.fromRequestToEntity(request);
        GameEntity createdGame = gameService.createGame(entity);
        return ResponseEntity.ok(gameControllerConverter.fromEntityToResponse(createdGame));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGame(@PathVariable UUID id, @RequestBody GameRequest request) {
        if (request == null)
            ErrorCode.INVALID_REQUEST.throwException();

        GameEntity entity = gameControllerConverter.fromRequestToEntity(request);
        GameEntity updatedGame = gameService.updateGame(id, entity);
        return ResponseEntity.ok(gameControllerConverter.fromEntityToResponse(updatedGame));
    }

    @PostMapping("/{id}/revert/{versionId}")
    public ResponseEntity<GameResponse> revertGame(
            @PathVariable UUID id,
            @PathVariable UUID versionId,
            @RequestBody GameRevertRequest request) {
        if (request == null || request.getAuthorId() == null)
            ErrorCode.INVALID_REQUEST.throwException();

        GameEntity revertedGame = gameService.revertGame(id, versionId, request.getAuthorId());
        return ResponseEntity.ok(gameControllerConverter.fromEntityToResponse(revertedGame));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
