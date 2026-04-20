package fr.epita.backend.domain.service;

import fr.epita.backend.data.model.RefreshTokenModel;
import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.RefreshTokenRepository;
import fr.epita.backend.utils.ErrorCode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HexFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshExpirationMs;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${security.jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String createRefreshToken(UserModel user) {
        String rawToken = generateRefreshToken();

        RefreshTokenModel refreshToken = new RefreshTokenModel();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshExpirationMs));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    public UserModel validateRefreshToken(String rawToken) {
        RefreshTokenModel refreshToken = getRefreshToken(rawToken);

        if (refreshToken.isRevoked()) {
            throw ErrorCode.INVALID_REFRESH_TOKEN.toException();
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw ErrorCode.REFRESH_TOKEN_EXPIRED.toException();
        }

        return refreshToken.getUser();
    }

    public void revokeRefreshToken(String rawToken) {
        RefreshTokenModel refreshToken = getRefreshToken(rawToken);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    private RefreshTokenModel getRefreshToken(String rawToken) {
        return refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .orElseThrow(ErrorCode.INVALID_REFRESH_TOKEN::toException);
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
