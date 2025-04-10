package org.frequency.frequencyapi.payloads;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class PostRequest {
    private UUID authorId;
    private String postType;

    private String singleSongId;
    private List<String> songIds;

    private Set<String> tagIds;
    private MultipartFile backgroundImage;
    private String caption;
}
