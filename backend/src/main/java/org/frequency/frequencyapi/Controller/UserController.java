package org.frequency.frequencyapi.Controller;

import org.frequency.frequencyapi.Models.User;
import org.frequency.frequencyapi.MySQLRepositories.UserRepo;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserRepo repository;

    public UserController(UserRepo repository) {
        this.repository = repository;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) throws Exception {

        return repository.findById(id).orElseThrow(() -> new Exception("User not found"));
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        return repository.findById(id).map(
                user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setPassword(updatedUser.getPassword());
                    user.setEmail(updatedUser.getEmail());
                    return repository.save(updatedUser);
                })
                .orElseGet(() -> repository.save(updatedUser));
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable String id) {
        repository.deleteById(id);
    }


}
