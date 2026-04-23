package fr.epita.backend.config;

import fr.epita.backend.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;

import static org.mockito.Mockito.*;

class UserStatusAuthenticationFilterTest {

    private final UserRepository repository = mock(UserRepository.class);

    private final UserStatusAuthenticationFilter filter =
            new UserStatusAuthenticationFilter(repository);

    @Test
    void should_continue_if_no_jwt() throws Exception {

        // WHY:
        // Si pas de JWT → le filtre ne bloque pas

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}