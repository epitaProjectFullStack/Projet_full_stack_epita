package fr.epita.backend.config;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final UserStatusAuthenticationFilter userStatusAuthenticationFilter;
  private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;
  private final ApiAccessDeniedHandler apiAccessDeniedHandler;

  @Value("${app.cors.allowed-origins}")
  private List<String> allowedOrigins;

  public SecurityConfig(
      UserStatusAuthenticationFilter userStatusAuthenticationFilter,
      ApiAuthenticationEntryPoint apiAuthenticationEntryPoint,
      ApiAccessDeniedHandler apiAccessDeniedHandler) {
    this.userStatusAuthenticationFilter = userStatusAuthenticationFilter;
    this.apiAuthenticationEntryPoint = apiAuthenticationEntryPoint;
    this.apiAccessDeniedHandler = apiAccessDeniedHandler;
  }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
            var config = new CorsConfiguration();
            config.setAllowedOrigins(allowedOrigins);
            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }))
        .authorizeHttpRequests(
            auth -> auth
                // WEBSOCKET DOIT PASSER
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/games/review")
                .hasAnyRole("MODERATOR", "ADMINISTRATOR")
                .requestMatchers(HttpMethod.GET, "/api/games/all")
                .permitAll()
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
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(apiAuthenticationEntryPoint)
            .accessDeniedHandler(apiAccessDeniedHandler))
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(buildJwtAuthenticationConverter())))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterAfter(userStatusAuthenticationFilter, BearerTokenAuthenticationFilter.class);
  
    return http.build();
}
  @Bean
  public JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret) {
    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    return NimbusJwtDecoder.withSecretKey(secretKey).build();
  }

  private Converter<Jwt, ? extends AbstractAuthenticationToken> buildJwtAuthenticationConverter() {
    return jwt -> {
      String role = jwt.getClaimAsString("role");
      List<SimpleGrantedAuthority> authorities = role == null
          ? List.of()
          : List.of(new SimpleGrantedAuthority("ROLE_" + role));
      return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    };
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy("""
        ROLE_ADMINISTRATOR > ROLE_MODERATOR
        ROLE_MODERATOR > ROLE_USER
        """);
    return hierarchy;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
}
