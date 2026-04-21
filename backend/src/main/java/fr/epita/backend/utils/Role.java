package fr.epita.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    USER(1),
    MODERATOR(2),
    ADMINISTRATOR(3);

    public Integer permissionLevel;
}
