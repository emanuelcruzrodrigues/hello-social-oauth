package com.emanuel.hello.service;

import com.emanuel.hello.domain.Account;
import com.emanuel.hello.domain.IdentityProvider;
import com.emanuel.hello.domain.Person;
import com.emanuel.hello.domain.SocialAuthenticator;
import com.emanuel.hello.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountFacadeImpl implements AccountFacade {

    private final AccountRepository accountRepository;

    @Override
    public Account register(Map<String, Object> attributes) {
        log.info("Register account [{}]", kv("account", attributes));


        final String subjectId = (String) attributes.get("sub");
        final IdentityProvider identityProvider = IdentityProvider.GOOGLE;
        List<Account> accounts = accountRepository.findBySocialAuthenticator(subjectId, identityProvider);
        if (!accounts.isEmpty()) {
            final Account account = accounts.getFirst();
            log.info("Account with subjectId={} and identityProvider={} already registered with id={}", subjectId, identityProvider, account.getId());
            return account;
        }

        final Person person = new Person();
        person.setName((String) attributes.get("name"));
        person.setGivenName((String) attributes.get("given_name"));
        person.setFamilyName((String) attributes.get("family_name"));

        final SocialAuthenticator socialAuthenticator = new SocialAuthenticator();
        socialAuthenticator.setEmail((String) attributes.get("email"));
        socialAuthenticator.setSubjectId(subjectId);
        socialAuthenticator.setIdentityProvider(identityProvider);

        Account account = accountRepository.getByPersonGivenName(person.getGivenName());
        if (account == null) {
            account = new Account();
            account.setPerson(person);
        }
        account.getSocialAuthenticators().add(socialAuthenticator);

        accountRepository.save(account);
        return account;
    }
}
