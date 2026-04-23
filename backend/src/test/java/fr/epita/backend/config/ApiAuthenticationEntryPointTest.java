package fr.epita.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.*;

class ApiAuthenticationEntryPointTest {

    private final ApiAuthenticationEntryPoint entryPoint =
            new ApiAuthenticationEntryPoint(new ObjectMapper().findAndRegisterModules());

    @Test
    void commence_should_return_401() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response,
                new BadCredentialsException("bad"));

        assertThat(response.getStatus()).isEqualTo(401);
    }
}