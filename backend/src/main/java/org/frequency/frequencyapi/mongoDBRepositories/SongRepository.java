package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SongRepository extends MongoRepository<Song, String> {
    List<Song> findBySpotifyId(String spotifyId);

    @Query("{ 'tagsByCategory.?0': ?1 }")
    List<Song> findByTagInCategory(String category, String tagValue);


    @Query("{ 'tagsByCategory.?0': { $exists: true } }")
    List<Song> findByTagCategoryExists(String category);
}
