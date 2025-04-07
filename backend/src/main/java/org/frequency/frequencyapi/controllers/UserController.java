package org.frequency.frequencyapi.controllers;

import org.frequency.frequencyapi.aws.S3Service;
import org.frequency.frequencyapi.models.Song;
import org.frequency.frequencyapi.models.User;
import org.frequency.frequencyapi.mongoDBRepositories.SongRepository;
import org.frequency.frequencyapi.mySQLRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> userOptional = userRepository.findById(uuid);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format");
        }
    }


    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/users/{id}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            String imageURL = s3Service.uploadFile(file);
            User user = optionalUser.get();
            user.setProfilePictureUrl(imageURL);
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading profile picture: " + e.getMessage());
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> userOptional = userRepository.findById(uuid);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                updates.forEach((key, value) -> {
                    switch (key) {
                        case "username": user.setUsername((String) value); break;
                        case "bio": user.setBio((String) value); break;
                        case "email": user.setEmail((String) value); break;
                        case "pronouns": user.setPronouns((String) value); break;
                    }
                });


                User updated = userRepository.save(user);
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format: " + id);

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            if (userRepository.existsById(uuid)) {
                userRepository.deleteById(uuid);
                return ResponseEntity.ok().body("User successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format: " + id);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/saved-songs")
    public ResponseEntity<?> getSavedSongs(@PathVariable UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        List<Song> savedSongs = songRepository.findAllById(user.getSavedSongIds());
        return ResponseEntity.ok(savedSongs);
    }

    @PatchMapping("/users/{userId}/save-song/{songId}")
    public ResponseEntity<?> saveSong(@PathVariable UUID userId, @PathVariable String songId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = optionalUser.get();
        user.getSavedSongIds().add(songId);
        userRepository.save(user);
        return ResponseEntity.ok("Song saved.");
    }

    @PatchMapping("/users/{userId}/unsave-song/{songId}")
    public ResponseEntity<?> unsaveSong(@PathVariable UUID userId, @PathVariable String songId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = optionalUser.get();
        user.getSavedSongIds().remove(songId);
        userRepository.save(user);
        return ResponseEntity.ok("Song unsaved.");
    }



}
