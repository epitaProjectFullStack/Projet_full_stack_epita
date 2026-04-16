package fr.epita.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final TokenAuthenticationFilter tokenAuthenticationFilter;

  public SecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter) {
    this.tokenAuthenticationFilter = tokenAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {

    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth
            -> auth.requestMatchers(HttpMethod.GET, "/api/games/review")
                   .hasAnyRole("MODERATOR", "ADMINISTRATOR")
                   .requestMatchers(HttpMethod.GET, "/api/games/all")
                   .hasRole("ADMINISTRATOR")
                   .requestMatchers(HttpMethod.PATCH, "/api/games/*/status")
                   .hasAnyRole("MODERATOR", "ADMINISTRATOR")
                   .requestMatchers("/api/games/**", "/api/auth/**",
                                    "/swagger-ui/**", "/v3/api-docs/**",
                                    "/*.css", "/*.js", "/images/**",
                                    "/favicon.ico", "/index.html", "/")
                   .permitAll()
                   .requestMatchers("/api/user/**")
                   .hasRole("USER")
                   .requestMatchers("/api/admin/**")
                   .hasRole("ADMINISTRATOR")
                   .anyRequest()
                   .authenticated())
        .addFilterBefore(tokenAuthenticationFilter,
                         UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("""
                ROLE_ADMINISTRATOR > ROLE_MODERATOR
                ROLE_MODERATOR > ROLE_USER
                ROLE_USER > ROLE_GUEST
                """);
        return hierarchy;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
