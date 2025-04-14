package org.frequency.frequencyapi.payloads;

import lombok.Getter;
import lombok.Setter;
import org.frequency.frequencyapi.util.PostType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostRequest {
    private PostType postType;

    private String singleSongId;
    private List<String> songIds;

    private List<String> tagIds;
    private MultipartFile backgroundImage;
    private String caption;
}
