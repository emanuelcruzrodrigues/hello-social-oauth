package com.emanuel.hello.repository;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.domain.IdentityProvider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AccountRepositoryMongo extends MongoRepository<Account, String>, AccountRepository {

    @Query(value = "{'socialAuthenticators.subjectId': ?0, 'socialAuthenticators.identityProvider': ?1}")
    List<Account> findBySocialAuthenticator(String subjectId, IdentityProvider identityProvider);

    @Query(value = "{'person.name': /^?0$/i}")
    Account getByPersonName(String name);
}
