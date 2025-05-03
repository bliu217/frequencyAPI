package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Like;
import org.frequency.frequencyapi.models.PostLike;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface postLikeRepository extends MongoRepository<PostLike, String> {
    List<Like> findByPostId(String postId);
    Optional<Like> findByUserIdAndPostId(UUID userId, String postId);
}
