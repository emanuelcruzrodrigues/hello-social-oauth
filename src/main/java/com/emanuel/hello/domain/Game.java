package com.emanuel.hello.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "games")
@TypeAlias("GAME")
@ToString
public class Game{
    @Id
    private String id;
    private String name;
}
