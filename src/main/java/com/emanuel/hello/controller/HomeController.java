package com.emanuel.hello.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final Environment environment;

    @GetMapping("/")
    public String login(Model model, HttpServletRequest request) {
        addProviderAvailabilityFlag(model, "auth0");
        addProviderAvailabilityFlag(model, "azure");
        addProviderAvailabilityFlag(model, "facebook");
        addProviderAvailabilityFlag(model, "github");
        addProviderAvailabilityFlag(model, "google");
        addProviderAvailabilityFlag(model, "okta");

        if (!"replace-me".equals(environment.getProperty("steam.api.key", String.class))) {
            model.addAttribute("steam", true);
        }

        return "index";
    }

    private void addProviderAvailabilityFlag(Model model, String provider) {
        if (environment.containsProperty(String.format("spring.security.oauth2.client.registration.%s.client-id", provider))) {
            model.addAttribute(provider, true);
        }
    }

    @PostMapping("/logout")
    public String logout(Model model) {
        return "logout";
    }
}
