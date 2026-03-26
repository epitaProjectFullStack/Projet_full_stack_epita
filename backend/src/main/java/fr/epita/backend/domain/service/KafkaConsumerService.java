package fr.epita.backend.domain.service;

import fr.epita.backend.domain.event.GameEventMessage;
import fr.epita.backend.utils.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

  @KafkaListener(topics = KafkaTopics.GAME_EVENTS)
  public void listen(GameEventMessage message) {

    // WHY: preuve que Kafka fonctionne
    LOGGER.info("EVENT RECEIVED: {}", message);
  }
}
