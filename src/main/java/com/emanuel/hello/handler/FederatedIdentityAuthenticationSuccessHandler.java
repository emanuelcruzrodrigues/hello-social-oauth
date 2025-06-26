package com.emanuel.hello.handler;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.domain.IdentityProvider;
import com.emanuel.hello.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountService accountService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        final DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
        log.info("The user {} has logged in.", userDetails.getName());

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                token.getAuthorizedClientRegistrationId(),
                token.getName()
        );

        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            log.info("Access Token: {}", accessToken);
        } else {
            log.warn("No authorized client or access token found for user {}", userDetails.getName());
        }

        final IdentityProvider identityProvider = IdentityProvider.get(token.getAuthorizedClientRegistrationId());
        final Account account = accountService.register(identityProvider, userDetails.getAttributes());

        final String contextPath = StringUtils.isBlank(request.getContextPath()) ? "/account" : request.getContextPath();
        request.getSession().setAttribute("accountId", account.getId());
        request.getSession().setAttribute("identityProvider", identityProvider);
        response.sendRedirect(contextPath);
    }
}
