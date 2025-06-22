package com.emanuel.hello.repository;

import com.emanuel.hello.domain.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepositoryMongo extends MongoRepository<Game, String>, GameRepository {

}
