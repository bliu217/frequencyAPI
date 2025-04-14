package org.frequency.frequencyapi.util;

import lombok.Getter;

@Getter
public class PostStats {
    private int likes;
    private int reposts;
    private int saves;

    public PostStats() {
        this.likes = 0;
        this.reposts = 0;
        this.saves = 0;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void incrementReposts() {
        this.reposts++;
    }
    public void incrementSaves() {
        this.saves++;
    }
}
