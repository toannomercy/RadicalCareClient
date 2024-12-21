package org.example.radicalmotor.Configs;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
public class SecurityConfig {

    private final Dotenv dotenv = Dotenv.load();
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        String clientId = dotenv.get("OAUTH2_GOOGLE_CLIENT_ID");
        String clientSecret = dotenv.get("OAUTH2_GOOGLE_CLIENT_SECRET");

        if (clientId == null || clientSecret == null) {
            throw new IllegalArgumentException("Missing OAUTH2_GOOGLE_CLIENT_ID or OAUTH2_GOOGLE_CLIENT_SECRET");
        }

        ClientRegistration googleClientRegistration = ClientRegistration.withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scope("profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();

        return new InMemoryClientRegistrationRepository(googleClientRegistration);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**",
                                        "/js/**",
                                        "/jquery/**",
                                        "/scss/**",
                                        "/Theme/**",
                                        "/img/**",
                                        "/images/profiles/**",
                                        "/jquery/**",
                                        "/lib/**",
                                        "/oauth2/**",
                                        "/auth/register/**",
                                        "/auth/login/**",
                                        "/error/**",
                                        "/auth/forgotPassword/**",
                                        "/auth/resetPassword/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/products").permitAll()
                        .requestMatchers(("/vehicles")).permitAll()
                        .requestMatchers(HttpMethod.GET,"/vehicles/detail/**" ).permitAll()
                        .requestMatchers("/services").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/forgotPassword").permitAll()
                        .requestMatchers( "/cart/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login") // Trang login tùy chỉnh
                        .defaultSuccessUrl("/") // Redirect sau khi login thành công
                        .failureUrl("/auth/login?error=true") // Redirect nếu login thất bại
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .deleteCookies("token")
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/error"))
                .httpBasic(httpBasic -> httpBasic.realmName("RadicalMotor"));

        return http.build();
    }



    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
