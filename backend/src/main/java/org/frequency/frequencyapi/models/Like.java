package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "likes")
@CompoundIndexes({
        @CompoundIndex(name = "uniq_user_post_like", def = "{'userId': 1, 'postId': 1}", unique = true)
})
public class Like {
    @Id
    private String id;


    @Indexed(name = "idx_like_userId")
    private UUID userId;

    @Indexed(name = "idx_like_postId")
    private String postId;
    private Instant createdAt;

    public Like(UUID userId, String postId) {
        this.userId = userId;
        this.postId = postId;
        this.createdAt = Instant.now();
    }
}
