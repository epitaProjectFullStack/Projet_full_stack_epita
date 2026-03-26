package fr.epita.backend.domain.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import fr.epita.backend.domain.event.GameEvent;
import fr.epita.backend.domain.event.GameEventMessage;
import fr.epita.backend.utils.KafkaTopics;

/**
 * WHY:
 * Ce service permet d'envoyer des événements Kafka
 * quand quelque chose se passe dans le système (ex: création de jeu)
 *
 * HOW:
 * Utilise KafkaTemplate fourni par Spring pour envoyer un message
 */
@Service
public class KafkaProducerService {

  private final KafkaTemplate<String, GameEventMessage> kafkaTemplate;

  public KafkaProducerService(KafkaTemplate<String, GameEventMessage> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * WHY:
   * Notifier les autres parties du système qu’un jeu a été créé
   *
   * HOW:
   * Envoie un message simple dans le topic "game-events"
   */
  public void sendGameCreated(UUID gameId) {
    GameEventMessage kafkamessage = new GameEventMessage(GameEvent.GAME_CREATED, gameId, Instant.now());
    kafkaTemplate.send(KafkaTopics.GAME_EVENTS, kafkamessage);
  }

  /**
   * WHY:
   * Envoyer un événement de modification
   */
  public void sendGameUpdated(String gameId) {
    GameEventMessage kafkamessage = new GameEventMessage(GameEvent.GAME_UPDATED, UUID.fromString(gameId),
        Instant.now());
    kafkaTemplate.send(KafkaTopics.GAME_EVENTS, kafkamessage);
  }

  /**
   * WHY:
   * Envoyer un événement de suppression
   */
  public void sendGameDeleted(String gameId) {
    GameEventMessage kafkamessage = new GameEventMessage(GameEvent.GAME_DELETED, UUID.fromString(gameId),
        Instant.now());
    kafkaTemplate.send(KafkaTopics.GAME_EVENTS, kafkamessage);
  }
}
