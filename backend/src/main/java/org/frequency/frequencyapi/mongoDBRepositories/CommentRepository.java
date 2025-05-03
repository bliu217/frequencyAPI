package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByPostId(String postId, Pageable pageable);
    List<Comment> findByAuthorId(UUID author_id);
}
