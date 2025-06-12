package com.emanuel.hello.service;

import com.emanuel.hello.domain.*;
import com.emanuel.hello.processor.AccountGameProcessor;
import com.emanuel.hello.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.*;
import static  com.emanuel.hello.utils.FieldExtractor.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService{

    private final AccountRepository accountRepository;
    private final AccountGameProcessor accountGameProcessor;

    public Account register(IdentityProvider identityProvider, Map<String, Object> attributes) {
        log.info("Register account [{}]", kv("account", attributes));

        String subjectId = extractSubjectId(attributes);
        List<Account> accounts = accountRepository.findBySocialAuthenticator(subjectId, identityProvider);
        //if already exists an account with the social authenticator, just update the account social authenticator
        if (!accounts.isEmpty()) {
            final Account account = accounts.getFirst();
            log.info("Account with subjectId={} and identityProvider={} already registered with id={}", subjectId, identityProvider, account.getId());
            updateAccountSocialAuthenticator(account, identityProvider, subjectId, attributes);
            return account;
        }

        return createOrUpdateAccount(identityProvider, attributes, subjectId);
    }

    private void updateAccountSocialAuthenticator(Account account, IdentityProvider identityProvider, String subjectId, Map<String, Object> attributes) {
        account.getSocialAuthenticators()
                .stream()
                .filter(socialAuthenticator -> identityProvider == socialAuthenticator.getIdentityProvider() && subjectId.equals(socialAuthenticator.getSubjectId()))
                .forEach(socialAuthenticator -> {
                    socialAuthenticator.setEmail(extractEmail(attributes));
                });
        accountRepository.save(account);
    }

    private Account createOrUpdateAccount(IdentityProvider identityProvider, Map<String, Object> attributes, String subjectId) {

        //dumb correlation rule: if already exists an account with the same given name, so it is the same person
        final Person person = createPerson(attributes);
        Account account = accountRepository.getByPersonGivenName(person.getGivenName());

        if (account == null) {
            account = new Account();
            account.setPerson(person);
        }

        final SocialAuthenticator socialAuthenticator = createSocialAuthenticator(identityProvider, attributes, subjectId);
        account.getSocialAuthenticators().add(socialAuthenticator);

        accountRepository.save(account);
        return account;
    }

    private Person createPerson(Map<String, Object> attributes) {
        final Person person = new Person();

        final String name = extractName(attributes, person);
        person.setName(name);

        final String givenName = extractGivenName(attributes, name);
        person.setGivenName(givenName);

        final String familyName = extractFamilyName(attributes, name, givenName);
        person.setFamilyName(familyName);
        return person;
    }

    private SocialAuthenticator createSocialAuthenticator(IdentityProvider identityProvider, Map<String, Object> attributes, String subjectId) {
        final SocialAuthenticator socialAuthenticator = new SocialAuthenticator();
        socialAuthenticator.setEmail(extractEmail(attributes));
        socialAuthenticator.setSubjectId(subjectId);
        socialAuthenticator.setIdentityProvider(identityProvider);
        return socialAuthenticator;
    }

    public void updateListOfGames(String accountId) {
        accountGameProcessor.downloadListOfGames(accountId);
    }

    public Account getAccount(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getGames() != null) {
            List<String> sortedIdsByPlaytime = account.getGames().keySet().stream()
                    .sorted((o1, o2) -> account.getGames().get(o2).getPlaytime().compareTo(account.getGames().get(o1).getPlaytime()))
                    .toList();

            LinkedHashMap<String, AccountGame> sortedGamesMap = new LinkedHashMap<>();
            sortedIdsByPlaytime.forEach(gameId -> sortedGamesMap.put(gameId, account.getGames().get(gameId)));
            account.setGames(sortedGamesMap);
        }

        return account;
    }

}
