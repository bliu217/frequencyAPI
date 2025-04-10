package org.frequency.frequencyapi.models;


import lombok.Getter;
import lombok.Setter;
import org.frequency.frequencyapi.payloads.SongSummary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "playlists")
public class Playlist {
    @Id
    private String id;

    private String name;
    private UUID userID;
    private String createdAt;
    private Set<SongSummary> songs;
    private Set<String> likedBy;
    private Visibility visibility;
}
