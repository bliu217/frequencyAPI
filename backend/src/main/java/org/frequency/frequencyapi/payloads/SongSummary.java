package org.frequency.frequencyapi.payloads;
import java.time.Instant;

public record SongSummary(String songId, Instant addedAt) {
}
