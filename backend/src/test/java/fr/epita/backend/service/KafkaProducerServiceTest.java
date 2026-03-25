package fr.epita.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import fr.epita.backend.domain.service.KafkaProducerService;

import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    private final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);

    private final KafkaProducerService service =
            new KafkaProducerService(kafkaTemplate);

    @Test
    void sendGameCreated_should_send_message() {
        service.sendGameCreated("123");

        verify(kafkaTemplate).send("game-events", "GAME_CREATED:123");
    }
}