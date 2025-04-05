package org.frequency.frequencyapi.MySQLRepositories;

import org.frequency.frequencyapi.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
