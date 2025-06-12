package com.emanuel.hello.config;

import com.emanuel.hello.handler.FederatedIdentityAuthenticationSuccessHandler;
import com.emanuel.hello.handler.LogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final FederatedIdentityAuthenticationSuccessHandler federatedIdentityAuthenticationSuccessHandler;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login/steam", "/login/steam/callback").permitAll()
                        .requestMatchers("/account").authenticated()
                        .anyRequest().permitAll())

                .oauth2Login(a -> a.successHandler(federatedIdentityAuthenticationSuccessHandler))

                .securityContext(securityContext -> securityContext
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                )

                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessHandler(logoutHandler))
                ;
        return http.build();
    }

}
