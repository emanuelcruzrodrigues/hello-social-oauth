package com.emanuel.hello.service;

import com.emanuel.hello.domain.*;
import com.emanuel.hello.processor.AccountGameProcessor;
import com.emanuel.hello.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService{

    private final AccountRepository accountRepository;
    private final AccountGameProcessor accountGameProcessor;

    public Account register(IdentityProvider identityProvider, Map<String, Object> attributes) {
        log.info("Register account [{}]", kv("account", attributes));

        final String subjectId = (String) attributes.get("sub");
        List<Account> accounts = accountRepository.findBySocialAuthenticator(subjectId, identityProvider);
        if (!accounts.isEmpty()) {
            final Account account = accounts.getFirst();
            log.info("Account with subjectId={} and identityProvider={} already registered with id={}", subjectId, identityProvider, account.getId());
            return account;
        }

        final Person person = new Person();

        String name = (String) attributes.get("name");
        person.setName(name);

        String givenName = (String) attributes.get("given_name");
        if (givenName == null) {
            givenName = name.split(" ")[0].trim();
        }
        person.setGivenName(givenName);

        String familyName = (String) attributes.get("family_name");
        if (familyName == null) {
            familyName = name.replace(givenName, "").trim();
        }
        person.setFamilyName(familyName);

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
