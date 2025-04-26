package m2.miage.interop.serviceauthentification.config;


import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import m2.miage.interop.serviceauthentification.dao.UtilisateurRepository;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceauthentification.modele.Utilisateur;
import m2.miage.interop.serviceauthentification.dto.UtilisateurDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final KeyGeneratorService keyGeneratorService;
    private final UtilisateurRepository utilisateurRepository;

    public SecurityConfig(KeyGeneratorService keyGeneratorService, UtilisateurRepository utilisateurRepository) {
        this.keyGeneratorService = keyGeneratorService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        // Réinitialisation du mdp
                        .requestMatchers(HttpMethod.POST, "/escamiage/utilisateur/reinitialisation/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/escamiage/utilisateur/connexion/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/escamiage/utilisateur/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/escamiage/utilisateur/refresh-token/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.PUT, "escamiage/utilisateur/password-reset/*").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt((jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(keyGeneratorService.getPublicKey())
                .privateKey(keyGeneratorService.getPrivateKey())
                .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }


    /*@Bean
    public JwtDecoder jwtDecoder(JWK jwk) {
        return NimbusJwtDecoder.withSecretKey(jwk.toOctetSequenceKey().toSecretKey()).build();
    }*/

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyGeneratorService.getPublicKey()).build();
    }

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public BiFunction<UtilisateurDTO, TypeToken, String> genereTokenFunction() {
        return (personne, type) -> {  // On passe "access" ou "refresh"
            Instant now = Instant.now();
            long expiry = type.equals(TypeToken.ACCESS_TOKEN) ? 3600L : 24 * 3600L; // 1h pour access, 1 jour pour refresh

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(String.valueOf(personne.getId()))
                    .claim("scope", personne.getRole())
                    .claim("email", personne.getEmail())
                    .claim("type", type.toString())
                    .build();

            JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();

            return jwtEncoder().encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        };
    }



    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }


    //METHODE UTILES POUR LE REFRESH TOKEN
    public boolean isAccessToken(Jwt jwt) {
        String type = jwt.getClaimAsString("type");
        return TypeToken.ACCESS_TOKEN.toString().equals(type); // Seul un access token est valide pour l'API
    }

    @Bean
    public Function<String, UtilisateurDTO> validateRefreshTokenFunction() throws UtilisateurInexistantException,JwtException {
        return refreshToken -> {
            Jwt jwt = jwtDecoder().decode(refreshToken);
            if (!TypeToken.REFRESH_TOKEN.toString().equals(jwt.getClaimAsString("type"))) {
                throw new JwtException("Token invalide");
            }
            Utilisateur utilisateur = utilisateurRepository.findById(Long.parseLong(jwt.getSubject())).orElseThrow(UtilisateurInexistantException::new);
            return new UtilisateurDTO(utilisateur.getId());
        };
    }




}
