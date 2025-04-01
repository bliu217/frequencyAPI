package org.frequency.frequencyapi.Repo;

import org.frequency.frequencyapi.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
