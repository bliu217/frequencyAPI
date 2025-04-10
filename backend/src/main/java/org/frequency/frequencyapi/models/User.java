package org.frequency.frequencyapi.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Column(unique=true)
    @NotNull
    private String username;

    @Column
    @NotNull
    private String password;

    @Column(unique=true)
    @NotNull
    private String email;

    @Column
    private String profilePictureUrl;

    @Column
    private String bio;

    @Column
    private String pronouns;

    @Column
    private String location;

    @Column
    private int followerCount = 0;

    @Column
    private int followingCount = 0;

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"),
            indexes = {
                    @Index(name = "user_id_idx", columnList = "user_id"),
                    @Index(name = "follower_id_idx", columnList = "follower_id")
            }
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_followings",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"),
            indexes = {
                @Index(name = "user_id_idx", columnList = "user_id"),
                @Index(name = "following_id_idx", columnList = "following_id")
            }
    )
    private Set<User> followings = new HashSet<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_saved_songs", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "song_id")
    private Set<String> savedSongIds = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_posts", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "post_id")
    private Set<String> postIds = new HashSet<>();


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {

    }

    public void follow(User user) {
        this.addFollowing(user);
        user.addFollower(this);
    }

    public void addFollower(User user) {
        if (this.followers.add(user))
            this.followerCount += 1;
    }

    public void addFollowing(User user) {
        if (this.followings.add(user))
            this.followingCount += 1;

    }

    public void unfollow(User user) {
        this.removeFollowing(user);
        user.removeFollower(this);
    }

    public void removeFollower(User user) {
        if (this.followers.remove(user))
            this.followerCount -= 1;
    }

    public void removeFollowing(User user) {
        if (this.followings.remove(user))
            this.followingCount -= 1;
    }
}
