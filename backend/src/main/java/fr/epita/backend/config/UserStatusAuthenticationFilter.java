package fr.epita.backend.config;

import fr.epita.backend.data.model.UserModel;
import fr.epita.backend.data.repository.UserRepository;
import fr.epita.backend.utils.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class UserStatusAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public UserStatusAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (!(SecurityContextHolder.getContext()
                .getAuthentication() instanceof JwtAuthenticationToken jwtAuthentication)) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID userId;
        try {
            userId = UUID.fromString(jwtAuthentication.getToken().getSubject());
        } catch (IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            throw new BadCredentialsException(ErrorCode.BAD_CREDENTIAL.getMessage(), e);
        }

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException(ErrorCode.BAD_CREDENTIAL.getMessage()));

        if (userModel.isBanned()) {
            SecurityContextHolder.clearContext();
            throw new AccessDeniedException(ErrorCode.BANNED_USER.getMessage());
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userModel.getLogin(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + userModel.getRole().name())));

        authentication.setDetails(jwtAuthentication.getDetails());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
