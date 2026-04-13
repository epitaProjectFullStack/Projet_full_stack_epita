package fr.epita.backend.service;

import fr.epita.backend.converter.DataConverter.UserDataConverter;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDataConverter converter = mock(UserDataConverter.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserService service = new UserService(userRepository, converter, passwordEncoder);

    @Test
    void getUser_should_return_user() {
        UUID id = UUID.randomUUID();
        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        UserEntity result = service.getUser(id);

        assertThat(result).isNotNull();
        verify(userRepository).findById(id);
        verify(converter).fromModelToEntity(model);
    }

    @Test
    void getUser_should_fail_if_not_found() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUser(id))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getUsers_should_return_list() {
        UserModel model = new UserModel();

        when(userRepository.findAll()).thenReturn(List.of(model));
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        List<UserEntity> result = service.getUsers();

        assertThat(result).isNotEmpty();
        verify(converter).fromModelToEntity(model);
    }

    @Test
    void createUser_should_create_user() {
        UserEntity entity = new UserEntity();
        entity.setLogin("alice");
        entity.setMail("alice@mail.com");
        entity.setPassword("pwd");

        UserModel model = new UserModel();

        when(converter.fromEntityToModel(entity)).thenReturn(model);
        when(userRepository.findByLogin("alice")).thenReturn(Optional.empty());
        when(userRepository.findByMail("alice@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pwd")).thenReturn("hashed-pwd");
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        service.createUser(entity);

        verify(passwordEncoder).encode("pwd");
        assertThat(model.getPassword()).isEqualTo("hashed-pwd");
        verify(userRepository).save(model);
    }

    @Test
    void createUser_should_fail_if_invalid_entity() {
        UserEntity entity = new UserEntity();

        assertThatThrownBy(() -> service.createUser(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_should_fail_if_login_exists() {
        UserEntity entity = new UserEntity();
        entity.setLogin("alice");
        entity.setMail("mail");
        entity.setPassword("pwd");

        when(userRepository.findByLogin("alice"))
                .thenReturn(Optional.of(new UserModel()));

        assertThatThrownBy(() -> service.createUser(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_should_fail_if_mail_exists() {
        UserEntity entity = new UserEntity();
        entity.setLogin("alice");
        entity.setMail("mail");
        entity.setPassword("pwd");

        when(userRepository.findByLogin("alice")).thenReturn(Optional.empty());
        when(userRepository.findByMail("mail"))
                .thenReturn(Optional.of(new UserModel()));

        assertThatThrownBy(() -> service.createUser(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_should_fail_on_db_error() {
        UserEntity entity = new UserEntity();
        entity.setLogin("alice");
        entity.setMail("mail");
        entity.setPassword("pwd");

        UserModel model = new UserModel();

        when(converter.fromEntityToModel(entity)).thenReturn(model);
        when(userRepository.findByLogin("alice")).thenReturn(Optional.empty());
        when(userRepository.findByMail("mail")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pwd")).thenReturn("hashed-pwd");

        doThrow(DataIntegrityViolationException.class)
                .when(userRepository).save(model);

        assertThatThrownBy(() -> service.createUser(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateUser_should_update_user() {
        UUID id = UUID.randomUUID();

        UserEntity entity = new UserEntity();
        entity.setLogin("new");
        entity.setMail("mail");
        entity.setPassword("pwd");

        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));
        when(passwordEncoder.encode("pwd")).thenReturn("hashed-pwd");
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        service.updateUser(id, entity);

        verify(converter).transfertDataFromEntityToModel(model, entity);
        verify(passwordEncoder).encode("pwd");
        assertThat(model.getPassword()).isEqualTo("hashed-pwd");
        verify(userRepository).save(model);
    }

    @Test
    void updateUser_should_fail_if_invalid() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();

        assertThatThrownBy(() -> service.updateUser(id, entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteUser_should_delete() {
        UUID id = UUID.randomUUID();
        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));

        service.deleteUser(id);

        verify(userRepository).delete(model);
    }

    @Test
    void deleteUser_should_fail_if_not_found() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteUser(id))
                .isInstanceOf(RuntimeException.class);
    }
}
