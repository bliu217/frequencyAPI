package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.CommentLike;
import org.frequency.frequencyapi.models.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface commentLikeRepository extends MongoRepository<CommentLike, String> {

    List<Like> findByCommentId(String commentId);
    Optional<Like> findByUserIdAndCommentId(UUID userId, String commentId);

}
