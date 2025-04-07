package org.frequency.frequencyapi.payloads;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PostRequest {
    private String authorId;
    private String postType;

    private String singleSongId;
    private List<String> songIds;

    private Set<String> tagIds;
    private MultipartFile backgroundImage;
    private String caption;
}
