package org.frequency.frequencyapi.Models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @OneToMany(mappedBy = "post")
    protected Set<Comment> comments;

    @ManyToMany
    protected Set<User> likes;
    protected int likeCount;
    protected int reposts;
    protected int saves;

    protected String authorId;

    @ManyToMany
    protected Set<Tag> tags;
    public String type;

    public Post(String authorId) {
        this.comments = new HashSet<>();
        this.likes = new HashSet<>();
        this.likeCount = 0;
        this.reposts = 0;
        this.saves = 0;
        this.authorId = authorId;
    }

    public Post() {

    }
}
