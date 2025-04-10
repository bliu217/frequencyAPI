package org.frequency.frequencyapi.mySQLRepositories;

import org.frequency.frequencyapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<Object> findByEmail(String usernameOrEmail);

    Optional<Object> findByUsername(String usernameOrEmail);

    @Query("select u from User u join u.followings f where f.id = :userId")
    Page<User> findFollowersByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("select u from User u join u.followers f where f.id = :userId")
    Page<User> findFollowingsByUserId(@Param("userId") UUID userId, Pageable pageable);
}
