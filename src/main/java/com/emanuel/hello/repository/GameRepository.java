package com.emanuel.hello.repository;

import com.emanuel.hello.domain.Game;

import java.util.Optional;

public interface GameRepository {
    Optional<Game> findById(String gameId);

    Game save(Game game);

    boolean existsById(String gameId);
}
