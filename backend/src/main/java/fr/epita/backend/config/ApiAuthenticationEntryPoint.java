package fr.epita.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epita.backend.controller.api.response.ApiErrorResponse;
import fr.epita.backend.utils.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public ApiAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        String message = resolveMessage(authException);
        writeResponse(response, HttpStatus.UNAUTHORIZED, message, request.getRequestURI());
    }

    private String resolveMessage(AuthenticationException authException) {
        if (authException instanceof InvalidBearerTokenException invalidBearerTokenException) {
            String message = invalidBearerTokenException.getMessage();
            if (message != null && message.toLowerCase().contains("expired")) {
                return ErrorCode.TOKEN_EXPIRED.getMessage();
            }
            return message != null ? message : ErrorCode.BAD_CREDENTIAL.getMessage();
        }

        return authException.getMessage() != null
                ? authException.getMessage()
                : ErrorCode.BAD_CREDENTIAL.getMessage();
    }

    private void writeResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message,
            String path) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiErrorResponse body = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
