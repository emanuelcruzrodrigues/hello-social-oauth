package com.emanuel.hello.domain;

import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@ToString
public class Account {
    private String id;
    private Person person;
    private Set<SocialAuthenticator> socialAuthenticators;
    private Map<String, AccountGame> games;

    public Account() {
        setId(UUID.randomUUID().toString());
        setSocialAuthenticators(new HashSet<>());
    }
}
