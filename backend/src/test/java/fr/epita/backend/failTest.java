package fr.epita.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FailTest {

    @Test
    void shouldFail() {
        fail(" Test volontairement cassé pour vérifier la CI");
    }
}