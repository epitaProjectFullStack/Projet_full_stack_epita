package fr.epita.backend.domain.service;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.AuthTokens;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.utils.ErrorCode;
import fr.epita.backend.utils.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            RefreshTokenService refreshTokenService,
            UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    public AuthTokens auth(String login, String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty())
            ErrorCode.INVALID_REQUEST.throwException();

        UserModel userModel = userRepository.findByLogin(login)
                .orElseThrow(ErrorCode.BAD_CREDENTIAL::toException);

        if (userModel.isBanned())
            ErrorCode.BANNED_USER.throwException();

        if (!passwordEncoder.matches(password, userModel.getPassword()))
            ErrorCode.BAD_CREDENTIAL.throwException();

        return new AuthTokens(
                tokenService.generateToken(userModel),
                refreshTokenService.createRefreshToken(userModel));
    }

    public AuthTokens refresh(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            ErrorCode.INVALID_REQUEST.throwException();
        }

        UserModel userModel = refreshTokenService.validateRefreshToken(rawRefreshToken);

        if (userModel.isBanned()) {
            ErrorCode.BANNED_USER.throwException();
        }

        refreshTokenService.revokeRefreshToken(rawRefreshToken);

        return new AuthTokens(
                tokenService.generateToken(userModel),
                refreshTokenService.createRefreshToken(userModel));
    }

    public void logout(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            ErrorCode.INVALID_REQUEST.throwException();
        }

        refreshTokenService.revokeRefreshToken(rawRefreshToken);
    }

    public UserEntity register(UserEntity entity) {
        entity.setRole(Role.USER);
        return userService.createUser(entity);
    }
}
