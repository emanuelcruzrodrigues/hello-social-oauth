package com.emanuel.hello.service;

import com.emanuel.hello.domain.Game;
import com.emanuel.hello.processor.GameProcessor;
import com.emanuel.hello.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameProcessor gameProcessor;

    public void downloadGameData(String gameId) {
        gameProcessor.process(gameId);
    }

    public Optional<Game> get(String gameId) {
        return gameRepository.findById(gameId);
    }
}
