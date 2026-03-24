package fr.epita.backend.controller.api.response.UserResponses;

import java.util.UUID;

import fr.epita.backend.utils.Role;
import lombok.Data;

@Data
public class UserResponse {
    private UUID id;
    private String login;
    private String mail;
    private Role role;
}
