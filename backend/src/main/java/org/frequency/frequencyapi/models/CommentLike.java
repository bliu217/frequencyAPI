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
@Document(collection = "comment_likes")
@CompoundIndexes({
        @CompoundIndex(name = "uniq_user_comment_like", def = "{'userId': 1, 'commentId': 1}", unique = true)
})
public class CommentLike extends Like {

    @Indexed
    private String commentId;


    public CommentLike(UUID uuid, String commentId) {
        super(uuid);
        this.commentId = commentId;
    }
}
