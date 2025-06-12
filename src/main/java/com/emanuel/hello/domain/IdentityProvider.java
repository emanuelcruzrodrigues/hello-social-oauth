package com.emanuel.hello.domain;

import java.util.Arrays;

public enum IdentityProvider {
    FACEBOOK,
    GITHUB,
    GOOGLE,
    OKTA,
    STEAM;

    public static IdentityProvider get(String authorizedClientRegistrationId) {
        return Arrays.stream(values())
                .filter(value -> value.toString().toLowerCase().equals(authorizedClientRegistrationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Identity provider not found"));
    }
}
