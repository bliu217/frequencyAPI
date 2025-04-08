package org.frequency.frequencyapi.mySQLRepositories;

import org.frequency.frequencyapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<Object> findByEmail(String usernameOrEmail);

    Optional<Object> findByUsername(String usernameOrEmail);
}
