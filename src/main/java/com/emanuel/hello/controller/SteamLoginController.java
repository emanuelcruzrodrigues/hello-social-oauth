package com.emanuel.hello.controller;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.domain.IdentityProvider;
import com.emanuel.hello.service.AccountService;
import com.emanuel.hello.service.SteamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class SteamLoginController {

    private final SteamService steamService;
    private final AccountService accountService;

    private static final String STEAM_LOGIN_URL = "https://steamcommunity.com/openid/login";
    private static final String RETURN_TO_URL = "http://localhost:8080/login/steam/callback"; // Change to your URL

    @GetMapping("/login/steam")
    public void steamLogin(HttpServletResponse response) throws IOException {
        String nonce = UUID.randomUUID().toString();

        String authUrl = STEAM_LOGIN_URL +
                "?openid.ns=http://specs.openid.net/auth/2.0" +
                "&openid.mode=checkid_setup" +
                "&openid.return_to=" + RETURN_TO_URL +
                "&openid.realm=http://localhost:8080/" + // Change to your domain
                "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select" +
                "&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select";

        response.sendRedirect(authUrl);
    }

    @GetMapping("/login/steam/callback")
    public String steamCallback(
            @RequestParam Map<String, String> params,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String steamId = extractSteamId(params.get("openid.claimed_id"));

        Authentication auth = new UsernamePasswordAuthenticationToken(
                "steam_" + steamId,  // principal
                null,                // credentials
                List.of(new SimpleGrantedAuthority("ROLE_USER")) // authorities
        );

        request.getSession(true);  // Force session creation
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(securityContext, request, response);

        Map<String, Object> playerSummary = steamService.getPlayerSummary(steamId);
        Map<String, Object> accountAttributes = new HashMap<>();
        accountAttributes.put("sub", steamId);
        accountAttributes.put("email", playerSummary.get("personaname"));
        String realName = (String) playerSummary.get("realname");
        String givenName = realName.split(" ")[0];
        accountAttributes.put("name", realName);
        accountAttributes.put("given_name", givenName.trim());
        accountAttributes.put("family_name", realName.replace(givenName, "").trim());

        final Account account = accountService.register(IdentityProvider.STEAM, accountAttributes);

        accountService.updateListOfGames(account.getId());

        request.getSession().setAttribute("accountId", account.getId());
        return "redirect:/account";
    }


    private String extractSteamId(String claimedId) {
        return claimedId.substring(claimedId.lastIndexOf("/") + 1);
    }

}