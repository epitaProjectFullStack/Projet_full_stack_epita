package fr.epita.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.*;

class ApiAccessDeniedHandlerTest {

    private final ApiAccessDeniedHandler handler =
            new ApiAccessDeniedHandler(new ObjectMapper().findAndRegisterModules());

    @Test
    void handle_should_return_403() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.handle(request, response, new AccessDeniedException("forbidden"));

        assertThat(response.getStatus()).isEqualTo(403);
    }
}