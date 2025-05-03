package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Post;
import org.frequency.frequencyapi.util.PostType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, String>{
    List<Post> findByAuthorId(UUID authorId);
    List<Post> findByPostType(PostType postType);
}
