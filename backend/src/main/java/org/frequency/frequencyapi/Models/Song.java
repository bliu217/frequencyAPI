package org.frequency.frequencyapi.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;
    private String artist;
    private String spotifyId;
    private String album;
    private String previewUrl;
    private String spotifyUrl;
    private String albumCoverUrl;

    @ManyToMany
    private Set<Tag> tags;

    public Song(String title, String artist, String spotifyId, String album, String previewUrl, String spotifyUrl, String albumCoverUrl) {
        this.title = title;
        this.artist = artist;
        this.spotifyId = spotifyId;
        this.album = album;
        this.previewUrl = previewUrl;
        this.spotifyUrl = spotifyUrl;
        this.albumCoverUrl = albumCoverUrl;
    }

    public Song() {

    }
}
