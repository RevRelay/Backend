package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface that uses JpaRepository to directly access database, handles user table
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserID(int userID);
    Optional<List<User>> findByDisplayNameContainingIgnoreCase(String displayName);
    Page<User> findByDisplayNameContainingIgnoreCase(String displayName, Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
