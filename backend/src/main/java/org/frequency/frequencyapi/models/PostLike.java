package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "post_likes")
@CompoundIndexes({
        @CompoundIndex(name = "uniq_user_post_like", def = "{'userId': 1, 'postId': 1}", unique = true)
})
public class PostLike extends Like{


    @Indexed(name = "idx_like_postId")
    private String postId;


    public PostLike(UUID uuid, String postId) {
        super(uuid);
        this.postId = postId;
    }
}
