package org.frequency.frequencyapi.Controller;

import org.frequency.frequencyapi.Models.User;
import org.frequency.frequencyapi.MySQLRepositories.UserRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    private final UserRepo repository;

    public UserController(UserRepo repository) {
        this.repository = repository;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return repository.findById(uuid)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format");
        }
    }


    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        UUID uuid = UUID.fromString(id);

        return repository.findById(uuid).map(
                user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setPassword(updatedUser.getPassword());
                    user.setEmail(updatedUser.getEmail());
                    return repository.save(updatedUser);
                })
                .orElseGet(() -> repository.save(updatedUser));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            if (repository.existsById(uuid)) {
                repository.deleteById(uuid);
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


}
