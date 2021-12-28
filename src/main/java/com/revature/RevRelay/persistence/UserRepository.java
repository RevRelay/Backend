package com.revature.RevRelay.persistence;

import com.revature.RevRelay.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAllByDisplayNameOrderByDisplayName(String displayName, Pageable pageable);
//    Optional<Users> findById(String username);
    Optional<User> findByUserID(int userID);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}