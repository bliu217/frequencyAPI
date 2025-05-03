package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Playlist;
import org.frequency.frequencyapi.util.Visibility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    List<Playlist> findByUserId(UUID userId);
    List<Playlist> findByUserIdAndVisibility(UUID userId, Visibility visibility);
}
