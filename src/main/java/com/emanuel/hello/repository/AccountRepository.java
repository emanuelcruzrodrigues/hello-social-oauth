package com.emanuel.hello.repository;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.domain.IdentityProvider;
import com.emanuel.hello.domain.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account, String> {

    @Query(value = "{'socialAuthenticators.subjectId': ?0, 'socialAuthenticators.identityProvider': ?1}")
    List<Account> findBySocialAuthenticator(String subjectId, IdentityProvider identityProvider);

    @Query(value = "{'person.givenName': ?0}")
    Account getByPersonGivenName(String givenName);
}
