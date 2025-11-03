package com.example.michelin_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter()))
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> (Collection<GrantedAuthority>) extractRoles(jwt));
        return converter;
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        var realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            roles.addAll((Collection<String>) realmAccess.get("roles"));
        }

        var resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey("coursm2")) {
            var client = (Map<String, Object>) resourceAccess.get("coursm2");
            if (client.containsKey("roles")) {
                roles.addAll((Collection<String>) client.get("roles"));
            }
        }

        return roles.stream()
                .map(r -> "ROLE_" + r.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder.withJwkSetUri("https://keycloak.bober.ovh/realms/master/protocol/openid-connect/certs")
                        .build();

        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer("https://keycloak.bober.ovh/realms/master");

        OAuth2TokenValidator<Jwt> withoutAudience = jwt -> OAuth2TokenValidatorResult.success();

        OAuth2TokenValidator<Jwt> validator =
                new DelegatingOAuth2TokenValidator<>(withIssuer, withoutAudience);

        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }
}
