package com.emanuel.hello.domain;

public enum IdentityProvider {
    GITHUB,
    GOOGLE,
    STEAM;

    public static IdentityProvider get(String authorizedClientRegistrationId) {
        return switch (authorizedClientRegistrationId) {
            case "github" -> GITHUB;
            case "google" -> GOOGLE;
            default -> throw new RuntimeException("Identity provider not found");
        };

    }
}
