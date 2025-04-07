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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_saved_songs", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "song_id")
    private Set<String> savedSongIds = new HashSet<>();


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {

    }
}
