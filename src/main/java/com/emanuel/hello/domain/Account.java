package com.emanuel.hello.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Document(collection = "accounts")
@TypeAlias("ACCOUNT")
@ToString
public class Account {
    @Id
    private String id;
    private Person person;
    private Set<SocialAuthenticator> socialAuthenticators;
    private Map<String, AccountGame> games;

    public Account() {
        setId(UUID.randomUUID().toString());
        setSocialAuthenticators(new HashSet<>());
    }
}
