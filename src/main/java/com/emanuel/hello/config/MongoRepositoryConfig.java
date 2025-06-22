package com.emanuel.hello.config;

import com.emanuel.hello.repository.AccountRepository;
import com.emanuel.hello.repository.AccountRepositoryMongo;
import com.emanuel.hello.repository.GameRepository;
import com.emanuel.hello.repository.GameRepositoryMongo;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

@Configuration
@ConditionalOnProperty(name = "spring.data.strategy", havingValue = "MONGO")
public class MongoRepositoryConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);  // Or use your Mongo URI
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, mongoDatabase);
    }

    @Bean
    public AccountRepository accountRepositoryMongo(MongoTemplate mongoTemplate) {
        MongoRepositoryFactory factory = new MongoRepositoryFactory(mongoTemplate);
        return factory.getRepository(AccountRepositoryMongo.class);
    }

    @Bean
    public GameRepository gameRepositoryMongo(MongoTemplate mongoTemplate) {
        MongoRepositoryFactory factory = new MongoRepositoryFactory(mongoTemplate);
        return factory.getRepository(GameRepositoryMongo.class);
    }

}
