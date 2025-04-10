package org.frequency.frequencyapi.controllers;

import jakarta.transaction.Transactional;
import org.frequency.frequencyapi.aws.S3Service;
import org.frequency.frequencyapi.models.Song;
import org.frequency.frequencyapi.models.User;
import org.frequency.frequencyapi.mongoDBRepositories.SongRepository;
import org.frequency.frequencyapi.mySQLRepositories.UserRepository;
import org.frequency.frequencyapi.payloads.UserSummary;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final SongRepository songRepository;

    @Autowired
    public UserController(UserRepository repository, S3Service s3Service, SongRepository songRepository) {
        this.userRepository = repository;
        this.s3Service = s3Service;
        this.songRepository = songRepository;
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(principal.getUser());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            Optional<User> user = userRepository.findById(UUID.fromString(id));
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            return ResponseEntity.ok(user.get());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid UUID");
        }

    }


    @PostMapping("/users/me/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@AuthenticationPrincipal CustomUserDetails principal, @RequestParam("file") MultipartFile file) {

        try {
            User user = principal.getUser();
            String imageURL = s3Service.uploadFile(file);
            user.setProfilePictureUrl(imageURL);
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading profile picture: " + e.getMessage());
        }
    }

    @PatchMapping("/users/me")
    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody Map<String, Object> updates) {
        try {
            User user = principal.getUser();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "username":
                        user.setUsername((String) value);
                        break;
                    case "bio":
                        user.setBio((String) value);
                        break;
                    case "email":
                        user.setEmail((String) value);
                        break;
                    case "pronouns":
                        user.setPronouns((String) value);
                        break;
                    default:
                        System.out.println("Ignored unknown field: " + key);
                        break;
                }
            });


            User updated = userRepository.save(user);
            return ResponseEntity.ok(updated);


        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }

    }

    @DeleteMapping("/users/me")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails principal) {
        try {
            User user = principal.getUser();
            userRepository.deleteById(user.getId());
            return ResponseEntity.ok().body("User successfully deleted");

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/users/me/saved-songs")
    public ResponseEntity<?> getSavedSongs(@AuthenticationPrincipal CustomUserDetails principal) {

        User user = principal.getUser();
        List<Song> savedSongs = songRepository.findAllById(user.getSavedSongIds());
        return ResponseEntity.ok(savedSongs);
    }

    @PatchMapping("/users/me/save-song/{songId}")
    public ResponseEntity<?> saveSong(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String songId) {

        User user = principal.getUser();
        user.getSavedSongIds().add(songId);
        userRepository.save(user);
        return ResponseEntity.ok("Song saved.");
    }

    @PatchMapping("/users/me/unsave-song/{songId}")
    public ResponseEntity<?> unsaveSong(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String songId) {

        User user = principal.getUser();
        user.getSavedSongIds().remove(songId);
        userRepository.save(user);
        return ResponseEntity.ok("Song unsaved.");
    }


    @GetMapping("/users/me/followers")
    public ResponseEntity<?> getFollowers(@AuthenticationPrincipal CustomUserDetails principal, @RequestParam int page, @RequestParam int size) {
        UUID uuid = principal.getUser().getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<User> followersPage = userRepository.findFollowersByUserId(uuid, pageable);
        Page<UserSummary> results = followersPage.map(u -> new UserSummary(u.getId(), u.getUsername(), u.getBio()));

        return ResponseEntity.ok(results);
    }

    @GetMapping("/users/me/following")
    public ResponseEntity<?> getFollowing(@AuthenticationPrincipal CustomUserDetails principal, @RequestParam int page, @RequestParam int size) {
        UUID uuid = principal.getUser().getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<User> followersPage = userRepository.findFollowingsByUserId(uuid, pageable);
        Page<UserSummary> results = followersPage.map(u -> new UserSummary(u.getId(), u.getUsername(), u.getBio()));

        return ResponseEntity.ok(results);
    }

    @Transactional
    @PatchMapping("/users/follow/{id}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable UUID id) {
        User currentUser = principal.getUser();

        if (currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("You cannot follow yourself.");
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User userToFollow = userOptional.get();

        if (currentUser.getFollowings().contains(userToFollow)) {
            return ResponseEntity.badRequest().body("You already follow this user.");
        }

        currentUser.follow(userToFollow);

//        userRepository.save(userToFollow); Transactional annotation fixes it, google it
        userRepository.save(currentUser);
        return ResponseEntity.ok("User followed.");

    }

    @Transactional
    @PatchMapping("/users/unfollow/{id}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable UUID id) {
        User currentUser = principal.getUser();

        if (currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("You cannot unfollow yourself.");
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User userToUnfollow = userOptional.get();

        if (!currentUser.getFollowings().contains(userToUnfollow)) {
            return ResponseEntity.badRequest().body("You don't follow this user.");
        }

        currentUser.unfollow(userToUnfollow);

//        userRepository.save(userToUnfollow); same as the follow method
        userRepository.save(currentUser);
        return ResponseEntity.ok("User unfollowed.");

    }


}
