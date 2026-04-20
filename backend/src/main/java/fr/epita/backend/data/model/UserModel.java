package fr.epita.backend.data.model;

import fr.epita.backend.utils.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserModel {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password; // a Hasher

    @Column(name = "mail_address", nullable = false, unique = true)
    private String mail;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "banned", nullable = false)
    private boolean banned;// A modifier en Objet comportant plus de détails Date du Ban / Raison du Ban /
                           // Durée du Ban
                           // Potentiellement un Enum Banned/Active ?
}
