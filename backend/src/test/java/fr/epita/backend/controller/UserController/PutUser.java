package fr.epita.backend.controller.UserController;

import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.api.response.UserResponses.UserResponse;
import fr.epita.backend.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PutUser {

    @LocalServerPort
    private int port;

    private RestClient client() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    private UserRequest validUserRequest(String login, String mail) {
        UserRequest request = new UserRequest();
        request.setLogin(login);
        request.setPassword("secret");
        request.setMail(mail);
        request.setRole(Role.USER);
        request.setToken("dummy-token");
        request.setBanned(false);
        return request;
    }

    @Test
    void Test_updateUser_valid() {
        ResponseEntity<UserResponse> created = client().post()
                .uri("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(validUserRequest("alice", "alice@mail.fr"))
                .retrieve()
                .toEntity(UserResponse.class);

        UserRequest update = validUserRequest("alice-updated", "alice.updated@mail.fr");

        ResponseEntity<UserResponse> updated = client().put()
                .uri("/api/user/" + created.getBody().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .retrieve()
                .toEntity(UserResponse.class);

        assertEquals(HttpStatus.OK, updated.getStatusCode());
        assertNotNull(updated.getBody());
        assertEquals(created.getBody().getId(), updated.getBody().getId());
        assertEquals("alice-updated", updated.getBody().getLogin());
        assertEquals("alice.updated@mail.fr", updated.getBody().getMail());
    }

    @Test
    void Test_updateUser_unknownId() {
        HttpClientErrorException.BadRequest exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> client().put()
                        .uri("/api/user/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(validUserRequest("alice", "alice@mail.fr"))
                        .retrieve()
                        .toEntity(String.class));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void Test_updateUser_emptyJson() {
        ResponseEntity<UserResponse> created = client().post()
                .uri("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(validUserRequest("alice", "alice@mail.fr"))
                .retrieve()
                .toEntity(UserResponse.class);

        HttpClientErrorException.BadRequest exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> client().put()
                        .uri("/api/user/" + created.getBody().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}")
                        .retrieve()
                        .toEntity(String.class));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
