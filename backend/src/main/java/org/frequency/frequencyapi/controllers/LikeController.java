package org.frequency.frequencyapi.controllers;

import org.frequency.frequencyapi.models.*;
import org.frequency.frequencyapi.mongoDBRepositories.CommentLikeRepository;
import org.frequency.frequencyapi.mongoDBRepositories.CommentRepository;
import org.frequency.frequencyapi.mongoDBRepositories.PostLikeRepository;
import org.frequency.frequencyapi.mongoDBRepositories.PostRepository;
import org.frequency.frequencyapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
public class LikeController {

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public LikeController(PostLikeRepository postLikeRepository, CommentLikeRepository commentLikeRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.postLikeRepository = postLikeRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }


    @GetMapping("/posts/{id}/likes")
    public ResponseEntity<?> getLikes(@PathVariable String id, @RequestParam int page, @RequestParam int size) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body("Page must be >= 0 and size must be > 0.");
        }

        if (!postRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }
        Pageable pageable = PageRequest.of(page, size);

        Page<Like> likes =  postLikeRepository.findByPostId(id, pageable);

        return ResponseEntity.ok(likes);
    }

    @PostMapping("/posts/{id}/likes")
    public ResponseEntity<?> togglePostLike(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }
        UUID userId = principal.getUser().getId();
        Optional<PostLike> optionalLike = postLikeRepository.findByUserIdAndPostId(userId, id);
        Post post = optionalPost.get();

        if (optionalLike.isPresent()) {
            PostLike like = optionalLike.get();
            postLikeRepository.delete(like);
            post.decrementLikesCount();
            postRepository.save(post);
            return ResponseEntity.ok("Like successfully unliked.");
        }

        PostLike newLike = new PostLike(userId, id);
        postLikeRepository.save(newLike);
        post.incrementLikesCount();
        postRepository.save(post);

        return ResponseEntity.ok("Like successfully liked.");

    }



    @PostMapping("/comments/{id}/likes")
    public ResponseEntity<?> toggleCommentLike(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found.");
        }
        UUID userId = principal.getUser().getId();
        Optional<CommentLike> optionalLike = commentLikeRepository.findByUserIdAndCommentId(userId, id);
        Comment comment = optionalComment.get();

        if (optionalLike.isPresent()) {
            CommentLike like = optionalLike.get();
            commentLikeRepository.delete(like);
            comment.decreaseLikes();
            commentRepository.save(comment);
            return ResponseEntity.ok("Like successfully unliked.");
        }

        CommentLike newLike = new CommentLike(userId, id);
        commentLikeRepository.save(newLike);
        comment.increaseLikes();
        commentRepository.save(comment);
        return ResponseEntity.ok("Like successfully liked.");

    }


}
