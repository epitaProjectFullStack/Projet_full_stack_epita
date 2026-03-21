package fr.epita.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    GUEST(0),
    USER(1),
    MODERATOR(2),
    ADMINISTRATOR(3);

    public Integer permissionLevel;
}
