package com.emanuel.hello.config;

import com.emanuel.hello.handler.FederatedIdentityAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final FederatedIdentityAuthenticationSuccessHandler federatedIdentityAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2Login(a -> a.successHandler(federatedIdentityAuthenticationSuccessHandler))
                .logout(logout -> logout.permitAll())
                ;
        return http.build();
    }

}
