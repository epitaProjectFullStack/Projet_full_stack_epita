package fr.epita.backend;

import org.junit.jupiter.api.Test;

class BackendApplicationTests {

    @Test
    void contextLoads() {
        // WHY: test de placeholder — le vrai démarrage nécessite PostgreSQL et Kafka
        // qui ne sont pas disponibles en CI sans Docker.
        // Les vrais tests unitaires sont dans les classes *ServiceTest et *ControllerTest.
    }
}