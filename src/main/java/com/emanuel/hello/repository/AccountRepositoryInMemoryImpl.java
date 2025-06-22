package com.emanuel.hello.repository;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.domain.IdentityProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AccountRepositoryInMemoryImpl {

    private final Map<String, Account> data = new HashMap<>();

    public List<Account> findBySocialAuthenticator(String subjectId, IdentityProvider identityProvider) {
        return data
                .values()
                .stream()
                .filter(account -> account
                        .getSocialAuthenticators()
                        .stream().anyMatch(socialAuthenticator ->
                                subjectId.equals(socialAuthenticator.getSubjectId())
                                        && identityProvider == socialAuthenticator.getIdentityProvider())
                ).collect(Collectors.toList());
    }

    public Account getByPersonGivenName(String givenName) {
        if (givenName == null) return null;
        return data
                .values()
                .stream()
                .filter(account -> {
                    String personGivenName = account.getPerson().getGivenName();
                    return personGivenName != null && StringUtils.equalsIgnoreCase(personGivenName, givenName);
                })
                .findFirst()
                .orElse(null);
    }

    public void save(Account account) {
        data.put(account.getId(), account);
    }

    public Optional<Account> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }
}
