package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Page<Users> findAllByOrderBydisplayName(String displayName,Pageable pageable);
    Optional<Users> findById(String username);
    Optional<Users> findByUserID(int userID);
}
