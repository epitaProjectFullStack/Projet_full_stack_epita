package fr.epita.backend.controller.UserController;

import fr.epita.backend.controller.api.request.UserRequest;
import fr.epita.backend.controller.api.response.UserResponses.UserResponse;
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
class CreateUser {

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
        void Test_createUser_valid() {
                UserRequest request = validUserRequest("alice", "alice@test.com");
                ResponseEntity<UserResponse> createdResponse = client().post()
                                .uri("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(request)
                                .retrieve()
                                .toEntity(UserResponse.class);

                assertEquals(HttpStatus.OK, createdResponse.getStatusCode());
                assertNotNull(createdResponse.getBody());
                assertEquals("alice", createdResponse.getBody().getLogin());
                assertEquals("alice@test.com", createdResponse.getBody().getMail());
                assertEquals("alice", createdResponse.getBody().getLogin());
        }

        @Test
        void Test_createUser_empty_request() {
                HttpClientErrorException.BadRequest exception = assertThrows(
                                HttpClientErrorException.BadRequest.class,
                                () -> client().post()
                                                .uri("/api/user")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body("{}")
                                                .retrieve()
                                                .toEntity(String.class));

                assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        }

        @Test
        void Test_createUser_no_body() {
                HttpClientErrorException.BadRequest exception = assertThrows(
                                HttpClientErrorException.BadRequest.class,
                                () -> client().post()
                                                .uri("/api/user")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .retrieve()
                                                .toEntity(String.class));

                assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        }

        @Test
        void Test_createSameUser() {
                UserRequest request = validUserRequest("alice", "alice@test.com");
                client().post().uri("/api/user").contentType(MediaType.APPLICATION_JSON)
                                .body(request)
                                .retrieve()
                                .toEntity(UserResponse.class);

                HttpClientErrorException.BadRequest exception = assertThrows(
                                HttpClientErrorException.BadRequest.class,
                                () -> client().post()
                                                .uri("/api/user")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(request)
                                                .retrieve()
                                                .toEntity(String.class));
                assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        }

}
