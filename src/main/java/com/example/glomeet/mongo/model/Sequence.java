package com.example.glomeet.mongo.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "sequence")
public class Sequence {

    @Id
    private String id;
    private Long seq;

}
