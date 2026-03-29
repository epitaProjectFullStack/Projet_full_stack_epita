package fr.epita.backend.service;

import fr.epita.backend.converter.DataConverter.UserDataConverter;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.domain.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDataConverter converter = mock(UserDataConverter.class);

    private final UserService service = new UserService(userRepository, converter);

    @Test
    void getUser_should_return_user() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving an existing user returns a converted entity
        // ============================================================

        // WHY:
        // Cas nominal → utilisateur trouvé en base

        UUID id = UUID.randomUUID();
        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        // HOW:
        // appel du service → conversion attendue

        UserEntity result = service.getUser(id);

        assertThat(result).isNotNull();
        verify(userRepository).findById(id);
        verify(converter).fromModelToEntity(model);
    }

    @Test
    void getUser_should_fail_if_not_found() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving unknown user throws exception
        // ============================================================

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // HOW:
        // appel → exception attendue

        assertThatThrownBy(() -> service.getUser(id))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getUsers_should_return_list() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Retrieving all users returns a list of converted entities
        // ============================================================

        UserModel model = new UserModel();

        when(userRepository.findAll()).thenReturn(List.of(model));
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        // HOW:
        // boucle interne → conversion de chaque élément

        List<UserEntity> result = service.getUsers();

        assertThat(result).isNotEmpty();
        verify(converter).fromModelToEntity(model);
    }

    @Test
    void createUser_should_create_user() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating a valid user saves it in repository
        // ============================================================

        UserEntity entity = new UserEntity();
        entity.setLogin("alice");
        entity.setMail("alice@mail.com");
        entity.setPassword("pwd");

        UserModel model = new UserModel();

        when(converter.fromEntityToModel(entity)).thenReturn(model);
        when(userRepository.findByLogin("alice")).thenReturn(Optional.empty());
        when(userRepository.findByMail("alice@mail.com")).thenReturn(Optional.empty());
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        // HOW:
        // appel → save + conversion retour

        service.createUser(entity);

        verify(userRepository).save(model);
    }

    @Test
    void createUser_should_fail_if_invalid_entity() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating user fails if required fields are missing
        // ============================================================

        UserEntity entity = new UserEntity(); // vide

        // HOW:
        // validation → exception

        assertThatThrownBy(() -> service.createUser(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createUser_should_fail_if_login_exists() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating user fails if login already exists
        // ============================================================

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

        // ============================================================
        // THIS TEST PROVES THAT:
        // Creating user fails if mail already exists
        // ============================================================

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

        // ============================================================
        // THIS TEST PROVES THAT:
        // DB constraint error is converted into business exception
        // ============================================================

        UserEntity entity = new UserEntity();
        entity.setLogin("alice");
        entity.setMail("mail");
        entity.setPassword("pwd");

        UserModel model = new UserModel();

        when(converter.fromEntityToModel(entity)).thenReturn(model);
        when(userRepository.findByLogin("alice")).thenReturn(Optional.empty());
        when(userRepository.findByMail("mail")).thenReturn(Optional.empty());

        doThrow(DataIntegrityViolationException.class)
                .when(userRepository).save(model);

        assertThatThrownBy(() -> service.createUser(entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateUser_should_update_user() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating a user modifies and saves it
        // ============================================================

        UUID id = UUID.randomUUID();

        UserEntity entity = new UserEntity();
        entity.setLogin("new");
        entity.setMail("mail");
        entity.setPassword("pwd");

        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));
        when(converter.fromModelToEntity(model)).thenReturn(new UserEntity());

        // HOW:
        // transfert + save

        service.updateUser(id, entity);

        verify(converter).transfertDataFromEntityToModel(model, entity);
        verify(userRepository).save(model);
    }

    @Test
    void updateUser_should_fail_if_invalid() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Updating fails if entity is invalid
        // ============================================================

        UUID id = UUID.randomUUID();

        UserEntity entity = new UserEntity(); // vide

        assertThatThrownBy(() -> service.updateUser(id, entity))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteUser_should_delete() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Deleting user calls repository delete
        // ============================================================

        UUID id = UUID.randomUUID();
        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));

        service.deleteUser(id);

        verify(userRepository).delete(model);
    }

    @Test
    void deleteUser_should_fail_if_not_found() {

        // ============================================================
        // THIS TEST PROVES THAT:
        // Deleting unknown user throws exception
        // ============================================================

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteUser(id))
                .isInstanceOf(RuntimeException.class);
    }
}