package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Getter
@Setter
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String text;
    private int likes = 0;

    @DBRef
    private User author;

    private Date date = new Date();

    @DBRef
    private Post post;

    public Comment() {}
}
