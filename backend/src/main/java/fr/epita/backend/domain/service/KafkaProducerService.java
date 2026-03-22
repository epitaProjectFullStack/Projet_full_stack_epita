package fr.epita.backend.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * WHY:
     * Notifier les autres parties du système qu’un jeu a été créé
     *
     * HOW:
     * Envoie un message simple dans le topic "game-events"
     */
    public void sendGameCreatedEvent(String gameId) {
        kafkaTemplate.send("game-events", "GAME_CREATED:" + gameId);
    }

    /**
     * WHY:
     * Envoyer un événement de modification
     */
    public void sendGameUpdated(String gameId) {
        kafkaTemplate.send("game-events", "GAME_UPDATED:" + gameId);
    }

    /**
     * WHY:
     * Envoyer un événement de suppression
     */
    public void sendGameDeleted(String gameId) {
        kafkaTemplate.send("game-events", "GAME_DELETED:" + gameId);
    }
}