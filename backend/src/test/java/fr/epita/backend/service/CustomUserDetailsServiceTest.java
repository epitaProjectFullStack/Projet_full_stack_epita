package fr.epita.backend.service;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.service.CustomUserDetailsService;
import fr.epita.backend.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private final UserRepository repository = mock(UserRepository.class);

    private final CustomUserDetailsService service =
            new CustomUserDetailsService(repository);

    @Test
    void loadUser_should_return_user_details() {

        // WHY:
        // Vérifie que Spring Security récupère correctement un utilisateur

        UserModel user = new UserModel();
        user.setLogin("john");
        user.setPassword("pwd");
        user.setBanned(false);
        user.setRole(Role.USER);

        when(repository.findByLogin("john"))
                .thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("john");

        assertThat(result.getUsername()).isEqualTo("john");
    }

    @Test
    void loadUser_should_fail_if_not_found() {

        when(repository.findByLogin("john"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("john"))
                .isInstanceOf(Exception.class);
    }
}