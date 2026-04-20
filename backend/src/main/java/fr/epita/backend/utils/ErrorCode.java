package fr.epita.backend.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_CREDENTIAL(HttpStatus.UNAUTHORIZED, "Bad credentials"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token expired"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token expired"),
    LOGIN_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Login already exist"),
    MAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Mail already exist"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Mail or Login already exist"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request"),
    UNREGISTERED(HttpStatus.BAD_REQUEST, "User inconnu"),
    BANNED_USER(HttpStatus.FORBIDDEN, "User is banned"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "Game not found"),
    GAME_VERSION_NOT_FOUND(HttpStatus.NOT_FOUND, "Game version not found"),
    GAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Game already exists");

    private final HttpStatus status;
    private final String message;

    public ResponseStatusException toException() {
        return new ResponseStatusException(status, message);
    }

    public ResponseStatusException toException(String prefix) {
        return new ResponseStatusException(status, prefix + ": " + message);
    }

    public void throwException() {
        throw toException();
    }

    public void throwException(String prefix) {
        throw toException(prefix);
    }
}
