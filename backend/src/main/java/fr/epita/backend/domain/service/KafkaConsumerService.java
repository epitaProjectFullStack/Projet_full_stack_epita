package fr.epita.backend.domain.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * WHY:
 * Permet de recevoir les événements Kafka pour audit/log
 *
 * HOW:
 * Spring écoute automatiquement le topic "game-events"
 */
@Service
public class KafkaConsumerService {

  @KafkaListener(topics = "game-events", groupId = "game-group")
  public void listen(String message) {

    // WHY: preuve que Kafka fonctionne
    System.out.println("EVENT RECEIVED: " + message);
  }
}
