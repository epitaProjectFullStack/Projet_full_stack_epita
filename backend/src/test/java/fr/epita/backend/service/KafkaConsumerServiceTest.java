package fr.epita.backend.service;

import fr.epita.backend.domain.event.GameEventMessage;
import fr.epita.backend.domain.service.KafkaConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

class KafkaConsumerServiceTest {

    private final SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);

    private final KafkaConsumerService service =
            new KafkaConsumerService(template);

    @Test
    void listen_should_forward_event_to_websocket() {

        // WHY:
        // Vérifie que Kafka redistribue bien les événements vers WebSocket

        GameEventMessage message = mock(GameEventMessage.class);

        service.listen(message);

        verify(template).convertAndSend("/topic/games", message);
    }
}