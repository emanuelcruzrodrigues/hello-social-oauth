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

public class AccountRepositoryInMemoryImpl implements AccountRepository {

    private final Map<String, Account> data = new HashMap<>();

    @Override
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

    @Override
    public Account getByPersonName(String name) {
        if (name == null) return null;
        return data
                .values()
                .stream()
                .filter(account -> {
                    String personName = account.getPerson().getName();
                    return personName != null && StringUtils.equalsIgnoreCase(personName, name);
                })
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account save(Account account) {
        data.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }
}
