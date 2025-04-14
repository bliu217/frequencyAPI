package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.frequency.frequencyapi.util.PostStats;
import org.frequency.frequencyapi.util.PostType;
import org.frequency.frequencyapi.util.Visibility;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Document(collection = "posts")
@CompoundIndexes({
        @CompoundIndex(name = "cmp_popular", def = "{'visibility': 1, 'stats.likes': -1, 'stats.reposts': -1, 'stats.saves': -1}")
})
public class Post {

    @Id
    private String id;

    private PostStats stats;

    private UUID authorId;

    @Indexed(name = "idx_post_tags")
    private List<String> tagIds;

    private PostType postType;

    @Indexed(name =  "idx_highlight_song")
    private String singleSongId;

    @Indexed(name =  "idx_post_songIds")
    private List<String> songIds;
    private String backgroundImageUrl;

    @TextIndexed
    private String caption;

    @Indexed(name = "idx_post_visibility")
    private Visibility visibility;

    @Indexed(name = "idx_post_createdAt")
    private Instant createdAt;

    public Post(UUID authorId) {
        this.authorId = authorId;
        this.tagIds = new ArrayList<>();
        this.createdAt = Instant.now();
        this.stats = new PostStats();
    }
}
