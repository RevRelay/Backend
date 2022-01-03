package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Layer for Groups
 * 
 * Interface using JpaRepository to access the database
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Page<Group> findGroupsByUserOwnerUserID(Integer userOwnerID, Pageable pageable);

}
