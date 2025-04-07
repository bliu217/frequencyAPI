package org.frequency.frequencyapi.mongoDBRepositories;

import org.frequency.frequencyapi.models.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByNameAndCategory(String name, String category);
    List<Tag> findByCategory(String category);
}

