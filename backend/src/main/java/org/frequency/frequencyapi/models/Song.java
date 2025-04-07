package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Set;


@Getter
@Setter
@Document(collection = "songs")
public class Song {

    @Id
    private String id;

    private String title;
    private String artist;
    private String spotifyId;
    private String album;
    private String previewUrl;
    private String spotifyUrl;
    private String albumCoverUrl;

    private Map<String, Set<String>> tagsByCategory;

    public Song(String title, String artist, String spotifyId, String album, String previewUrl, String spotifyUrl, String albumCoverUrl) {
        this.title = title;
        this.artist = artist;
        this.spotifyId = spotifyId;
        this.album = album;
        this.previewUrl = previewUrl;
        this.spotifyUrl = spotifyUrl;
        this.albumCoverUrl = albumCoverUrl;
    }

    public Song() {}
}
