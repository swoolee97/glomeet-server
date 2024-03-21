package com.example.glomeet.mongo.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "lastReadTime")
public class LastReadTime {
    @Id
    private String _id;
    @Field
    private String roomId;
    @Field
    private String email;
    @Field
    private Date at;
}
