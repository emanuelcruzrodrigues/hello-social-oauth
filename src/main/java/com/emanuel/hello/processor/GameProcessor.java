package com.emanuel.hello.processor;

import com.emanuel.hello.domain.Game;
import com.emanuel.hello.repository.GameRepository;
import com.emanuel.hello.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class GameProcessor {
    private final Deque<String> processingQueue = new ArrayDeque<>();
    private final Thread processor;

    public GameProcessor(SteamService steamService, GameRepository gameRepository) {
        processor = new Thread(() -> {
            while (true) {
                while (!processingQueue.isEmpty()) {
                    String gameId = processingQueue.pollFirst();
                    log.info("Processing game {}", gameId);

                    if (gameRepository.existsById(gameId)) {
                        log.info("Game already exists in DB, game skipped.");
                        continue;
                    }

                    try {
                        Optional<Map<String, Object>> gameDataOptional = Optional.ofNullable(steamService.getGame(gameId));
                        Map<String, Object> gameData = gameDataOptional.orElseGet(() -> {
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("name", "Not found");
                            return result;
                        });
                        Game game = new Game();
                        game.setId(gameId);
                        game.setName((String) gameData.get("name"));
                        gameRepository.save(game);
                    } catch (Exception e) {
                        log.error("Error processing game: {}", gameId, e);
                        processingQueue.add(gameId);
                    }
                }
                try {
                    log.debug("No games to process");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        processor.start();
    }

    public void process(String gameId) {
        processingQueue.add(gameId);
    }
}
