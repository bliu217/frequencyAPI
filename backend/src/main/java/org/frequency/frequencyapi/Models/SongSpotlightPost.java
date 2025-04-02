package org.frequency.frequencyapi.Models;

public class SongSpotlightPost extends Post{

    public SongSpotlightPost(String authorId) {
        super(authorId);
        this.type = "spotlight";
    }


}
