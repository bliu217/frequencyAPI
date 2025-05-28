package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Post;
import org.frequency.frequencyapi.util.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;
import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, String>{
    Page<Post> findByAuthorId(UUID authorId, Pageable pageable);
    Page<Post> findByPostType(PostType postType, Pageable pageable);
    Page<Post> findAllByIdIn(Set<String> postIds, Pageable pageable);

}
