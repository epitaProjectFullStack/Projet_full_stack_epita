package fr.epita.backend.controller.UserController;

import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.api.response.UserResponses.UserResponse;
import fr.epita.backend.controller.api.response.UserResponses.UsersResponse;
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
class DeleteUser {

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
    void Test_deleteUser_valid() {
        ResponseEntity<UserResponse> created = client().post()
                .uri("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(validUserRequest("alice", "alice@mail.fr"))
                .retrieve()
                .toEntity(UserResponse.class);

        ResponseEntity<Void> deleteResponse = client().delete()
                .uri("/api/user/" + created.getBody().getId())
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<UsersResponse> usersResponse = client().get()
                .uri("/api/user")
                .retrieve()
                .toEntity(UsersResponse.class);

        assertNotNull(usersResponse.getBody());
        assertTrue(usersResponse.getBody().getList().isEmpty());
    }

    @Test
    void Test_deleteUser_unknownId() {
        HttpClientErrorException.BadRequest exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> client().delete()
                        .uri("/api/user/" + UUID.randomUUID())
                        .retrieve()
                        .toEntity(String.class));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
