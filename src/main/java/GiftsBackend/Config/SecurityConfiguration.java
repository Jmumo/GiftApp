package GiftsBackend.Config;


import GiftsBackend.Config.Auth.CustomAccessDeniedHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration

@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers(
                                        "/api/v1/auth/register",
                                        "/api/v1/auth/authenticate",
                                        "/api/v1/profile/update/{email}",
                                        "/mobile-money/validation",
                                        "/api/v1/auth/refreshToken",
                                        "/mobile-money/confirmation",
                                        "/mobile-money/stk-transaction-result",
                                        "/swagger-ui.html",  // Swagger UI page
                                        "/swagger-ui/index.html#/",  // Swagger UI assets
                                        "/swagger-resources/**",  // Swagger resources (required for UI to function)
                                        "/v3/api-docs/**",  // OpenAPI 3 docs (Swagger spec)
                                        "/webjars/**",  // Swagger Webjars (JS and CSS)
                                        "/v2/api-docs"
                                ).permitAll()
                        .anyRequest()
                        .authenticated()
                ). sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless sessions
        )

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAccessDeniedHandler)  // Custom error handler
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)  // Custom logout handler
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())  // Clear security context on logout
                );

        return http.build();
    }



}
