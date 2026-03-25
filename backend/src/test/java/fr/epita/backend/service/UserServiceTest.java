package fr.epita.backend.service;

import fr.epita.backend.converter.DataConverter.UserDataConverter;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.service.UserService;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UserServiceTest {

    // UserService attend (UserRepository, UserDataConverter) — les deux doivent être mockés
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDataConverter userDataConverter = mock(UserDataConverter.class);

    private final UserService userService = new UserService(userRepository, userDataConverter);

    @Test
    void getUser_should_return_user() {
        UUID id = UUID.randomUUID();
        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));

        userService.getUser(id);

        verify(userRepository).findById(id);
        // Le converter est appelé pour transformer le model en entity
        verify(userDataConverter).fromModelToEntity(model);
    }
}