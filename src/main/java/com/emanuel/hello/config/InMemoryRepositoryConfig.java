package com.emanuel.hello.config;

import com.emanuel.hello.repository.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.data.strategy", havingValue = "IN_MEMORY")
public class InMemoryRepositoryConfig {

    @Bean
    public AccountRepository accountRepositoryMongo() {
        return new AccountRepositoryInMemoryImpl();
    }

    @Bean
    public GameRepository gameRepositoryMongo() {
        return new GameRepositoryInMemoryImpl();
    }
}
