package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Data Layer for Users
 * 
 * Interface using JpaRepository to access the database
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUserID(int userID);

    boolean existsByUsername(String username);
}
