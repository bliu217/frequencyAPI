package org.frequency.frequencyapi.util;

import org.frequency.frequencyapi.models.Post;
import org.frequency.frequencyapi.payloads.PostRequest;

public interface PostTypeHandler {
    void handle(PostRequest request, Post post) throws Exception;
}
