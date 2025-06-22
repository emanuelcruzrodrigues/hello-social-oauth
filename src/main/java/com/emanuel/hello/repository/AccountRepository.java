package com.emanuel.hello.repository;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.domain.IdentityProvider;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(String id);
    List<Account> findBySocialAuthenticator(String subjectId, IdentityProvider identityProvider);
    Account getByPersonName(String name);
}
