package org.frequency.frequencyapi.models;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class Like {
    @Id
    private String id;


    @Indexed(name = "idx_like_userId")
    private UUID userId;

    @Indexed
    private Instant createdAt;

    public Like(UUID userId) {
        this.userId = userId;
        this.createdAt = Instant.now();
    }
}
