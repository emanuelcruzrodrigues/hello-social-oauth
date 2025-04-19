package com.emanuel.hello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"subjectId", "identityProvider"})
public class SocialAuthenticator implements AccountAuthenticator {
    private String email;
    private String subjectId;
    private IdentityProvider identityProvider;
}
