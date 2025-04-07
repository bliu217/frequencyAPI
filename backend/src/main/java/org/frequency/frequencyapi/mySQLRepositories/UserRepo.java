package org.frequency.frequencyapi.mySQLRepositories;

import org.frequency.frequencyapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
}
