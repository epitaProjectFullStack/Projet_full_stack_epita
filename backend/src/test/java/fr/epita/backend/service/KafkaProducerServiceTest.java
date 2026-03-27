package fr.epita.backend.service;

import fr.epita.backend.domain.event.GameEventMessage;
import fr.epita.backend.domain.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    // WHY:
    // On mock Kafka → pas de vrai broker

    private final KafkaTemplate<String, GameEventMessage> kafkaTemplate =
            mock(KafkaTemplate.class);

    private final KafkaProducerService service =
            new KafkaProducerService(kafkaTemplate);

    @Test
    void sendGameCreated_should_send_message() {
        // WHY:
        // Vérifie que l’événement est envoyé

        UUID gameId = UUID.randomUUID();

        service.sendGameCreated(gameId);

        // HOW:
        // On vérifie que Kafka reçoit bien un message

        verify(kafkaTemplate).send(
                eq("game-events"),
                any(GameEventMessage.class)
        );
    }
}