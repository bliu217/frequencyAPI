package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String>{
    List<Post> findByAuthorId(String authorId);
    List<Post> findByPostType(String postType);
}
