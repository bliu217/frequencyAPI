package org.frequency.frequencyapi.controllers;

import jakarta.annotation.PostConstruct;
import org.frequency.frequencyapi.models.User;
import org.frequency.frequencyapi.mySQLRepositories.UserRepository;
import org.frequency.frequencyapi.payloads.AuthRequest;
import org.frequency.frequencyapi.payloads.AuthResponse;
import org.frequency.frequencyapi.security.CustomUserDetailsService;
import org.frequency.frequencyapi.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void logDatasource() throws SQLException {
        System.out.println("Connected to DB: " + dataSource.getConnection().getMetaData().getURL());
    }

    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        Optional<?> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }

        User user = new User();
        user.setUsername(CustomUserDetailsService.extractNameFromEmail(request.getEmail()));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        System.out.println("Saved user ID: " + user.getId());
        System.out.println("Saved to table? Check MySQL for 'user' or 'users'");

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Optional<Object> userOption = userRepository.findByEmail(request.getEmail());

        if (userOption.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid email or password.");
        }

        User user = (User) userOption.get();

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
