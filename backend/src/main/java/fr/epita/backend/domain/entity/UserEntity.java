package fr.epita.backend.domain.entity;

import java.util.UUID;

import fr.epita.backend.utils.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    private UUID id;
    private String login;
    private String password;
    private String mail;
    private Role role;
    private String token;
    private boolean banned;
}
