package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@Document(collection = "songs")
public class Song {

    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String artist;

    @TextIndexed
    private String spotifyId;

    @TextIndexed
    private String album;

    private String previewUrl;
    private String spotifyUrl;
    private String albumCoverUrl;

    @Indexed(name = "idx_song_bpm")
    private int bpm;

    @Indexed(name = "idx_song_usageCount")
    private int usageCount;

    @Indexed(name = "idx_song_addedAt")
    private Instant addedAt;

    @Indexed(name = "idx_song_tags")
    private List<String> tagIds;

    public Song(String title, String artist, String spotifyId, String album, String previewUrl, String spotifyUrl, String albumCoverUrl, int bpm) {
        this.title = title;
        this.artist = artist;
        this.spotifyId = spotifyId;
        this.album = album;
        this.previewUrl = previewUrl;
        this.spotifyUrl = spotifyUrl;
        this.albumCoverUrl = albumCoverUrl;
        this.usageCount = 1;
        this.addedAt = Instant.now();
        this.bpm = bpm;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }

    public void decrementUsageCount() {
        this.usageCount--;
    }

    public Song() {}
}
