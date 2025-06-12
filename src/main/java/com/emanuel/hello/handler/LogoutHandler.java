package com.emanuel.hello.handler;

import com.emanuel.hello.domain.IdentityProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Component
public class LogoutHandler implements LogoutSuccessHandler {

    @Value("${spring.security.oauth2.client.provider.okta.issuer-uri}")
    private String oktaIssuer;

    @Value("${spring.security.oauth2.client.registration.okta.client-id}")
    private String oktaClientId;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken
                && "okta".equals(((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId())) {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            response.sendRedirect(oktaIssuer + "v2/logout?client_id=" + oktaClientId + "&returnTo=" + baseUrl);
        } else {
            response.sendRedirect("/");
        }
    }
}
