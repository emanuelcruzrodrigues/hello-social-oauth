package com.emanuel.hello.handler;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.service.AccountFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountFacade accountFacade;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final DefaultOidcUser userDetails = (DefaultOidcUser) authentication.getPrincipal();
        final String email = userDetails.getEmail();

        log.info("The user {} has logged in.", email);
        final Account account = accountFacade.register(userDetails.getAttributes());

        final String contextPath = StringUtils.isBlank(request.getContextPath()) ? "/" : request.getContextPath();
        request.getSession().setAttribute("account", account);
        request.getSession().setAttribute("login", email);
        response.sendRedirect(contextPath);
    }


}
