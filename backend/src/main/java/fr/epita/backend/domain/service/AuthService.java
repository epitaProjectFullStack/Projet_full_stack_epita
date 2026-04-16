package fr.epita.backend.domain.service;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.utils.ErrorCode;
import fr.epita.backend.utils.Role;
import fr.epita.backend.converter.DataConverter.UserDataConverter;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserDataConverter userDataConverter;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthService(UserRepository userRepository, UserDataConverter userDataConverter,
            PasswordEncoder passwordEncoder, TokenService tokenService, UserService userService) {
        this.userRepository = userRepository;
        this.userDataConverter = userDataConverter;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    public UserEntity auth(String login, String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty())
            ErrorCode.INVALID_REQUEST.throwException();

        UserModel userModel = userRepository.findByLogin(login)
                .orElseThrow(ErrorCode.BAD_CREDENTIAL::toException);

        if (userModel.isBanned())
            ErrorCode.BANNED_USER.throwException();

        if (!passwordEncoder.matches(password, userModel.getPassword()))
            ErrorCode.BAD_CREDENTIAL.throwException();

        String token = tokenService.generateToken();
        userModel.setToken(token);
        userRepository.save(userModel);
        return userDataConverter.fromModelToEntity(userModel);
    }

    public UserEntity register(UserEntity entity) {
        entity.setRole(Role.USER);
        return userService.createUser(entity);
    }
}
