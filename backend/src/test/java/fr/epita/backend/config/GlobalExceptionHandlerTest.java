package fr.epita.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void should_handle_generic_exception() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseEntity<?> response =
                handler.handleGenericException(new RuntimeException(), request);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
    }
}