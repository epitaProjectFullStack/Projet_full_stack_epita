package fr.epita.backend.domain.service;

import fr.epita.backend.converter.DataConverter.GameDataConverter;
import fr.epita.backend.data.model.GameArticleVersionModel;
import fr.epita.backend.data.model.GameModel;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.GameArticleVersionRepository;
import fr.epita.backend.data.repository.GameRepository;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.GameArticleVersionEntity;
import fr.epita.backend.domain.entity.GameEntity;
import fr.epita.backend.utils.ErrorCode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional(readOnly = true)
public class GameService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

  private final GameRepository gameRepository;
  private final GameArticleVersionRepository gameArticleVersionRepository;
  private final UserRepository userRepository;
  private final GameDataConverter gameDataConverter;
  private final KafkaProducerService kafkaProducerService;

  public GameService(GameRepository gameRepository,
      GameArticleVersionRepository gameArticleVersionRepository,
      UserRepository userRepository,
      GameDataConverter gameDataConverter,
      KafkaProducerService kafkaProducerService) {
    this.gameRepository = gameRepository;
    this.gameArticleVersionRepository = gameArticleVersionRepository;
    this.userRepository = userRepository;
    this.gameDataConverter = gameDataConverter;
    this.kafkaProducerService = kafkaProducerService;
  }

  public List<GameEntity> getGames() {
    List<GameEntity> entities = new ArrayList<>();

    for (GameModel model : gameRepository.findAll()) {
      entities.add(gameDataConverter.fromModelToEntity(model));
    }

    return entities;
  }

  public GameEntity getGame(UUID id) {
    GameModel model = gameRepository.findById(id).orElseThrow(
        ErrorCode.GAME_NOT_FOUND::toException);
    return gameDataConverter.fromModelToEntity(model);
  }

  public List<GameArticleVersionEntity> getGameVersions(UUID id) {
    gameRepository.findById(id).orElseThrow(
        ErrorCode.GAME_NOT_FOUND::toException);

    List<GameArticleVersionEntity> entities = new ArrayList<>();
    for (GameArticleVersionModel model : gameArticleVersionRepository.findByGameUuidOrderByVersionNumberDesc(
        id)) {
      entities.add(gameDataConverter.fromVersionModelToEntity(model));
    }

    return entities;
  }

  @Transactional
  public GameEntity createGame(GameEntity entity) {
    validateWritableEntity(entity);
    ensureGameNameAvailable(entity.getSubjectGameName());

    UserModel author = findAuthor(entity.getAuthorId());

    GameModel gameModel = new GameModel();
    gameModel.setSubjectGameName(entity.getSubjectGameName());
    gameRepository.save(gameModel);

    GameArticleVersionModel versionModel = buildVersion(gameModel, author, entity, 1);
    gameArticleVersionRepository.save(versionModel);

    gameModel.setCurrentVersion(versionModel);
    gameRepository.save(gameModel);

    kafkaProducerService.sendGameCreated(gameModel.getUuid());

    return gameDataConverter.fromModelToEntity(gameModel);
  }

  @Transactional
  public GameEntity updateGame(UUID id, GameEntity entity) {
    validateWritableEntity(entity);

    GameModel gameModel = gameRepository.findById(id).orElseThrow(
        ErrorCode.GAME_NOT_FOUND::toException);
    UserModel author = findAuthor(entity.getAuthorId());

    if (!gameModel.getSubjectGameName().equals(entity.getSubjectGameName())) {
      ensureGameNameAvailable(entity.getSubjectGameName());
      gameModel.setSubjectGameName(entity.getSubjectGameName());
    }

    Integer nextVersion = gameModel.getCurrentVersion() == null
        ? 1
        : gameModel.getCurrentVersion().getVersionNumber() + 1;

    GameArticleVersionModel versionModel = buildVersion(gameModel, author, entity, nextVersion);
    gameArticleVersionRepository.save(versionModel);

    gameModel.setCurrentVersion(versionModel);
    gameRepository.save(gameModel);

    // kafkaProducerService.sendGameUpdated(gameModel.getUuid().toString());
    return gameDataConverter.fromModelToEntity(gameModel);
  }

  @Transactional
  public GameEntity revertGame(UUID gameId, UUID versionId, UUID authorId) {
    GameModel gameModel = gameRepository.findById(gameId).orElseThrow(
        ErrorCode.GAME_NOT_FOUND::toException);
    GameArticleVersionModel sourceVersion = gameArticleVersionRepository.findByUuidAndGameUuid(versionId, gameId)
        .orElseThrow(ErrorCode.GAME_VERSION_NOT_FOUND::toException);
    UserModel author = findAuthor(authorId);

    GameEntity revertEntity = new GameEntity();
    revertEntity.setAuthorId(authorId);
    revertEntity.setSubjectGameName(gameModel.getSubjectGameName());
    revertEntity.setArticleName(sourceVersion.getArticleName());
    revertEntity.setArticleContent(sourceVersion.getArticleContent());

    Integer nextVersion = gameModel.getCurrentVersion() == null
        ? 1
        : gameModel.getCurrentVersion().getVersionNumber() + 1;

    GameArticleVersionModel versionModel = buildVersion(gameModel, author, revertEntity, nextVersion);
    gameArticleVersionRepository.save(versionModel);

    gameModel.setCurrentVersion(versionModel);
    gameRepository.save(gameModel);

    return gameDataConverter.fromModelToEntity(gameModel);
  }

  @Transactional
  public void deleteGame(UUID id) {
    GameModel gameModel = gameRepository.findById(id).orElseThrow(
        ErrorCode.GAME_NOT_FOUND::toException);
    // kafkaProducerService.sendGameDeleted(id.toString());
    gameRepository.delete(gameModel);
  }

  private void validateWritableEntity(GameEntity entity) {
    if (entity == null || entity.getAuthorId() == null)
      ErrorCode.INVALID_REQUEST.throwException();

    if (entity.getSubjectGameName() == null ||
        entity.getSubjectGameName().isBlank())
      ErrorCode.INVALID_REQUEST.throwException("subjectGameName");

    if (entity.getArticleName() == null || entity.getArticleName().isBlank())
      ErrorCode.INVALID_REQUEST.throwException("articleName");

    if (entity.getArticleContent() == null ||
        entity.getArticleContent().isBlank())
      ErrorCode.INVALID_REQUEST.throwException("articleContent");
  }

  private UserModel findAuthor(UUID authorId) {
    UserModel author = userRepository.findById(authorId).orElseThrow(
        ErrorCode.USER_NOT_FOUND::toException);

    if (author.isBanned())
      ErrorCode.BANNED_USER.throwException();

    return author;
  }

  private void ensureGameNameAvailable(String subjectGameName) {
    if (gameRepository.findBySubjectGameName(subjectGameName).isPresent())
      ErrorCode.GAME_ALREADY_EXISTS.throwException();
  }

  private GameArticleVersionModel buildVersion(GameModel gameModel,
      UserModel author,
      GameEntity entity,
      Integer versionNumber) {
    GameArticleVersionModel versionModel = new GameArticleVersionModel();
    versionModel.setGame(gameModel);
    versionModel.setAuthor(author);
    versionModel.setArticleName(entity.getArticleName());
    versionModel.setArticleContent(entity.getArticleContent());
    versionModel.setVersionNumber(versionNumber);
    versionModel.setCreatedAt(Instant.now());
    return versionModel;
  }
}
