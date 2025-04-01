package org.frequency.frequencyapi.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Song {
    private String title;
    private String artist;
    private String spotifyId;
    private String album;
    private String previewUrl;
    private String spotifyUrl;
    private String albumCoverUrl;

    public Song(String title, String artist, String spotifyId, String album, String previewUrl, String spotifyUrl, String albumCoverUrl) {
        this.title = title;
        this.artist = artist;
        this.spotifyId = spotifyId;
        this.album = album;
        this.previewUrl = previewUrl;
        this.spotifyUrl = spotifyUrl;
        this.albumCoverUrl = albumCoverUrl;
    }

}
