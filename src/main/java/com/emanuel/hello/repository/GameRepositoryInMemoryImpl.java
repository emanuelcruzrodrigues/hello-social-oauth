package com.emanuel.hello.repository;

import com.emanuel.hello.domain.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameRepositoryInMemoryImpl implements GameRepository {

    private final Map<String, Game> data = new HashMap<>();

    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(data.get(gameId));
    }

    public Game save(Game game) {
        data.put(game.getId(), game);
        return game;
    }

    public boolean existsById(String gameId) {
        return data.containsKey(gameId);
    }
}
