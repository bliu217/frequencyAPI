package org.frequency.frequencyapi.controllers;

import org.frequency.frequencyapi.models.Comment;
import org.frequency.frequencyapi.models.Post;
import org.frequency.frequencyapi.mongoDBRepositories.CommentRepository;
import org.frequency.frequencyapi.mongoDBRepositories.PostRepository;
import org.frequency.frequencyapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final int MAX_COMMENT_LENGTH = 255;

    @Autowired
    public CommentController(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }


    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable String id, @RequestParam int page, @RequestParam int size) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body("Page must be >= 0 and size must be > 0.");
        }

        if (!postRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByPostId(id, pageable);

        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);

        if (commentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found.");
        }

        Comment toDelete = commentOptional.get();

        Optional<Post> postOptional = postRepository.findById(toDelete.getPostId());

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }

        Post post = postOptional.get();
        UUID userId = principal.getUser().getId();


        if (!userId.equals(toDelete.getAuthorId()) && !userId.equals(post.getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this comment.");
        }

        try {
            commentRepository.deleteById(toDelete.getId());
            return ResponseEntity.ok("Comment deleted.");

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }


    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String id, @RequestBody String commentText) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }

        if (commentText == null || commentText.isEmpty()) {
            return ResponseEntity.badRequest().body("Comment text cannot be empty.");
        }

        if (commentText.length() > MAX_COMMENT_LENGTH) {
            return ResponseEntity.badRequest().body(String.format("Comment text is longer than %d characters.", MAX_COMMENT_LENGTH));
        }

        Comment newComment = new Comment(id, commentText, principal.getUser().getId());

        commentRepository.save(newComment);

        return ResponseEntity.ok(newComment);

    }
}
