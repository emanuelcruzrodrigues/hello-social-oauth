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
import java.util.Arrays;
import java.util.List;

@Component
public class LogoutHandler implements LogoutSuccessHandler {

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}")
    private String auth0Issuer;

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String auth0ClientId;

    private static final List<String> LOGOUT_PROVIDERS = Arrays.asList("auth0", "okta");

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken
                && "auth0".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            response.sendRedirect(auth0Issuer + "v2/logout?client_id=" + auth0ClientId + "&returnTo=" + baseUrl);
        } else {
            response.sendRedirect("/");
        }
    }
}
