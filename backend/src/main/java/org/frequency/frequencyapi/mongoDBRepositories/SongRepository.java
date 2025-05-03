package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SongRepository extends MongoRepository<Song, String> {
    List<Song> findBySpotifyId(String spotifyId);

    List<Song> findByBpmBetweenAndTagIdsIn(int min, int max, List<String> tagIds);

}
