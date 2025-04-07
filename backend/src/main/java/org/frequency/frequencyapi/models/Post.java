package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @DBRef
    private Set<Comment> comments = new HashSet<>();

    @DBRef
    private Set<User> likes = new HashSet<>();

    private int likeCount;
    private int reposts;
    private int saves;

    private String authorId;

    @DBRef
    private Set<Tag> tags = new HashSet<>();

    private String postType;

    public Post(String authorId) {
        this.authorId = authorId;
    }

    public Post() {}
}
