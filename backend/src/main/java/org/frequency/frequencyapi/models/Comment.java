package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String text;
    private int likes = 0;


    private UUID authorId;

    @Indexed(name = "idx_comment_createdAt")
    private Instant createdAt;

    @Indexed(name = "idx_post")
    private String postId;

    public Comment() {
        this.createdAt = Instant.now();
    }
}
