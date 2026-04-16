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

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GetUser {

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
        return request;
    }

    @Test
    void Test_getUsers_empty_db() {

        ResponseEntity<UsersResponse> usersResponse = client().get()
                .uri("/api/user")
                .retrieve()
                .toEntity(UsersResponse.class);

        assertNotNull(usersResponse);
        assertNotNull(usersResponse.getBody().getList());
        assertEquals(HttpStatus.OK, usersResponse.getStatusCode());
        assertTrue(usersResponse.getBody().getList().isEmpty());
    }

    @Test
    void Test_getUsers_valid() {
        UserRequest request = validUserRequest("alice", "alice@mail.fr");
        client().post()
                .uri("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(UserResponse.class);

        ResponseEntity<UsersResponse> usersResponse = client().get()
                .uri("/api/user")
                .retrieve()
                .toEntity(UsersResponse.class);

        assertNotNull(usersResponse);
        assertNotNull(usersResponse.getBody().getList());
        assertEquals(HttpStatus.OK, usersResponse.getStatusCode());
        assertFalse(usersResponse.getBody().getList().isEmpty());
        assertEquals("alice", usersResponse.getBody().getList().get(0).getLogin());
    }

    @Test
    void Test_getUser_valid() {
        UserRequest request = validUserRequest("alice", "alice@mail.fr");
        ResponseEntity<UserResponse> userCreated = client().post()
                .uri("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(UserResponse.class);

        ResponseEntity<UserResponse> usersResponse = client().get()
                .uri("/api/user/" + userCreated.getBody().getId())
                .retrieve()
                .toEntity(UserResponse.class);

        assertNotNull(usersResponse);
        assertEquals(HttpStatus.OK, usersResponse.getStatusCode());
        assertEquals(usersResponse.getBody().getLogin(), userCreated.getBody().getLogin());
        assertEquals(usersResponse.getBody().getId(), userCreated.getBody().getId());
    }

    @Test
    void Test_getUser_invalid() {
        HttpClientErrorException.BadRequest exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> client().get()
                        .uri("/api/user/" + "3c303d29-2ddc-40f6-a63c-a1503977747")
                        .retrieve()
                        .toEntity(String.class));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
