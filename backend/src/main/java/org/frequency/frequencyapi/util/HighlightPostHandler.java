package org.frequency.frequencyapi.util;

import org.frequency.frequencyapi.models.Post;
import org.frequency.frequencyapi.payloads.PostRequest;

public class HighlightPostHandler implements PostTypeHandler {

    @Override
    public void handle(PostRequest request, Post post) throws Exception {
        if (request.getSingleSongId() == null) {
            throw new Exception("HIGHLIGHT post must include a singleSongId");
        }
        post.setSingleSongId(request.getSingleSongId());
    }
}
