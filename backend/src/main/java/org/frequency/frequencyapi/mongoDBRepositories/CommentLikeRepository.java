package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.CommentLike;
import org.frequency.frequencyapi.models.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentLikeRepository extends MongoRepository<CommentLike, String> {

    Optional<CommentLike> findByUserIdAndCommentId(UUID userId, String commentId);

}
