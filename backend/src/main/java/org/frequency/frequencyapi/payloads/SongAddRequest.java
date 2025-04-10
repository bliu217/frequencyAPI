package org.frequency.frequencyapi.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongAddRequest {
    private String songId;
    private String addedAt;
    private String playlistName;
}
