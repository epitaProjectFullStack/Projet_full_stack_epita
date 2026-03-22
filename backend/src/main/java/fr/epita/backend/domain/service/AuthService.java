package fr.epita.backend.domain.service;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.domain.entity.UserEntity;
import fr.epita.backend.utils.ErrorCode;
import fr.epita.backend.converter.DataConverter.UserDataConverter;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserDataConverter userDataConverter;

    public AuthService(UserRepository userRepository, UserDataConverter userDataConverter) {
        this.userRepository = userRepository;
        this.userDataConverter = userDataConverter;
    }

    public UserEntity auth(String login, String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty())
            ErrorCode.INVALID_REQUEST.throwException();

        UserModel userModel = userRepository.findByLogin(login)
                .orElseThrow(ErrorCode.BAD_CREDENTIAL::toException);

        if (userModel.isBanned())
            ErrorCode.BANNED_USER.throwException();

        if (!userModel.getPassword().equals(password))
            ErrorCode.BAD_CREDENTIAL.throwException();

        return userDataConverter.fromModelToEntity(userModel);
    }
}
