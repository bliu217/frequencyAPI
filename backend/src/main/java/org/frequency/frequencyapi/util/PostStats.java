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

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;

        }
    }
    public void decrementReposts() {
        if (this.reposts > 0) {
            this.reposts--;
        }
    }
    public void decrementSaves() {
        if (this.saves > 0) {
            this.saves--;
        }
    }
}
