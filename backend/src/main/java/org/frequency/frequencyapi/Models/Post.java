package org.frequency.frequencyapi.Models;

import java.util.List;

public interface Post {

    List<Comment> comments = List.of();
    int likes = 0;
    int reposts = 0;
    int saves = 0;
    Content content();


}
