package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SongRepository extends MongoRepository<Song, String> {
    Optional<Song> findBySpotifyId(String spotifyId);

    @Query("{ 'bpm': { $gte: ?0, $lte: ?1 }, 'tagIds': { $all: ?2 } }")
    Page<Song> findByBpmBetweenAndTagIdsAll(int min, int max, List<String> tagIds, Pageable pageable);

}
