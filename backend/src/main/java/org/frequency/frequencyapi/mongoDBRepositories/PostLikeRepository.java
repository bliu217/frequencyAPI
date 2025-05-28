package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Like;
import org.frequency.frequencyapi.models.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends MongoRepository<PostLike, String> {
    Page<Like> findByPostId(String postId, Pageable pageable);
    Optional<PostLike> findByUserIdAndPostId(UUID userId, String postId);
}
