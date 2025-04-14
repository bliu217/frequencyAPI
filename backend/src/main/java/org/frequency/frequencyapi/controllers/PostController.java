package org.frequency.frequencyapi.controllers;

import org.frequency.frequencyapi.aws.S3Service;
import org.frequency.frequencyapi.models.Post;
import org.frequency.frequencyapi.models.User;
import org.frequency.frequencyapi.security.CustomUserDetails;
import org.frequency.frequencyapi.util.PostType;
import org.frequency.frequencyapi.payloads.PostRequest;
import org.frequency.frequencyapi.mongoDBRepositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Autowired
    public PostController(PostRepository postRepository, S3Service s3Service) {
        this.postRepository = postRepository;
        this.s3Service = s3Service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable String id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            return ResponseEntity.ok(optionalPost.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String postId) {
        User user = principal.getUser();
        return postRepository.findById(postId).map(post -> {
            if (!post.getAuthorId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post.");
            }
            postRepository.deleteById(postId);
            return ResponseEntity.ok("Post deleted.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found."));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePostCaption(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String postId, @RequestBody PostRequest request) {
        User user = principal.getUser();

        return postRepository.findById(postId).map(post -> {
            if (!post.getAuthorId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this post.");
            }

            if (request.getCaption() != null) {
                post.setCaption(request.getCaption());
                postRepository.save(post);
                return ResponseEntity.ok(post);
            } else {
                return ResponseEntity.badRequest().body("Caption is missing in the request.");
            }

        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found."));
    }


    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal CustomUserDetails principal, @ModelAttribute PostRequest request) {
        PostType postType = request.getPostType();
        User user = principal.getUser();

        Post post = new Post(user.getId());
        post.setPostType(postType);
        post.setTagIds(request.getTagIds());
        post.setCaption(request.getCaption());

        switch (postType) {
            case HIGHLIGHT:
                if (request.getSingleSongId() == null) {
                    return ResponseEntity.badRequest().body("HIGHLIGHT post must include a singleSongId");
                }
                post.setSingleSongId(request.getSingleSongId());
                break;

            case CAROUSEL:
                if (request.getSongIds() == null || request.getSongIds().size() > 10) {
                    return ResponseEntity.badRequest().body("CAROUSEL post must include up to 10 songIds");
                }
                post.setSongIds(request.getSongIds());
                break;

            case MOODBOARD:
                if (request.getSongIds() == null || request.getSongIds().size() > 10) {
                    return ResponseEntity.badRequest().body("MOODBOARD post must include up to 10 songIds");
                }
                post.setSongIds(request.getSongIds());

                MultipartFile bgImage = request.getBackgroundImage();
                if (bgImage == null || bgImage.isEmpty()) {
                    return ResponseEntity.badRequest().body("MOODBOARD post must include a background image");
                }

                try {
                    String bgUrl = s3Service.uploadFile(bgImage);
                    post.setBackgroundImageUrl(bgUrl);
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().body("Error uploading background image: " + e.getMessage());
                }
                break;

            default:
                return ResponseEntity.badRequest().body("Invalid postType: " + postType);
        }

        Post saved = postRepository.save(post);
        return ResponseEntity.ok(saved);
    }
}
