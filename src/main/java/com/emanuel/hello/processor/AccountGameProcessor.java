package com.emanuel.hello.processor;

import com.emanuel.hello.domain.AccountGame;
import com.emanuel.hello.domain.Game;
import com.emanuel.hello.domain.IdentityProvider;
import com.emanuel.hello.domain.SocialAuthenticator;
import com.emanuel.hello.repository.AccountRepository;
import com.emanuel.hello.service.GameService;
import com.emanuel.hello.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class AccountGameProcessor {

    private final Deque<String> processingQueue = new ArrayDeque<>();
    private final Thread processor;

    public AccountGameProcessor(AccountRepository accountRepository, GameService gameService, SteamService steamService) {
        processor = new Thread(() -> {
            while (true) {
                while (!processingQueue.isEmpty()) {
                    String accountId = processingQueue.pollFirst();
                    log.info("Processing account {}", accountId);

                    try {
                        accountRepository.findById(accountId).ifPresent(account -> {
                            account.getSocialAuthenticators().stream()
                                    .filter(socialAuthenticator -> IdentityProvider.STEAM == socialAuthenticator.getIdentityProvider())
                                    .map(SocialAuthenticator::getSubjectId)
                                    .findFirst()
                                    .ifPresent(steamId -> {
                                        List<Map<String, Object>> listOfGames = steamService.getPlayerListOfGames(steamId);
                                        boolean success = true;
                                        for (Map<String, Object> gameData : listOfGames) {
                                            String appId = gameData.get("appid").toString();
                                            Optional<Game> gameOptional = gameService.get(appId);
                                            if (gameOptional.isEmpty()) {
                                                success = false;
                                                gameService.downloadGameData(appId);
                                            } else if (success){
                                                if (account.getGames() == null) {
                                                    account.setGames(new HashMap<>());
                                                }
                                                AccountGame accountGame = account.getGames().getOrDefault(appId, new AccountGame());
                                                accountGame.setName(gameOptional.get().getName());
                                                accountGame.setPlaytime(((Number) gameData.get("playtime_forever")).longValue());
                                                account.getGames().put(appId, accountGame);
                                            }
                                        }
                                        if (success) {
                                            accountRepository.save(account);
                                        } else {
                                            processingQueue.add(accountId);
                                        }
                                    });
                        });
                    } catch (Exception e) {
                        log.error("Error processing account {}", accountId, e);
                    }
                }
                try {
                    log.info("No accounts to process");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        processor.start();
    }

    public void downloadListOfGames(String accountId) {
        processingQueue.add(accountId);
    }
}
