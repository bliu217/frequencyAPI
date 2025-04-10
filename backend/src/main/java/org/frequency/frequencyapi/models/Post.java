package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @DBRef
    private Set<Comment> comments = new HashSet<>();

    private Set<String> likedByUserIds = new HashSet<>();

    private int likeCount;
    private int reposts;
    private int saves;

    private UUID authorId;

    private Set<String> tagIds = new HashSet<>();

    private String postType;
    private String singleSongId;
    private List<String> songIds;
    private String backgroundImageUrl;
    private String caption;

    public Post(UUID authorId) {
        this.authorId = authorId;
    }

    public Post() {}
}
