package org.frequency.frequencyapi.models;


import lombok.Getter;
import lombok.Setter;
import org.frequency.frequencyapi.payloads.SongSummary;
import org.frequency.frequencyapi.util.Visibility;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Document(collection = "playlists")
@CompoundIndexes({
        @CompoundIndex(name = "idx_songIds", def = "{'songs.songId': 1}")
})
public class Playlist {
    @Id
    private String id;

    @TextIndexed
    private String name;

    @Indexed(name = "idx_playlist_userId")
    private UUID userId;

    @Indexed(name = "idx_playlist_createdAt")
    private Instant createdAt;
    private List<SongSummary> songs;
    private Set<String> likedBy;

    @Indexed(name = "idx_playlist_visibility")
    private Visibility visibility;

    @TextIndexed
    private String description;

    @Indexed(name = "idx_playlist_tags")
    private List<String> tagIds;

    public Playlist(String name, UUID userId) {
        this.name = name;
        this.userId = userId;
        this.createdAt = Instant.now();
        this.songs = new ArrayList<>();
        this.likedBy = new HashSet<>();
    }

}
