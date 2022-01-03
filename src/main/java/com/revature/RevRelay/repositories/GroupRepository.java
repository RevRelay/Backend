package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface that uses JpaRepository to directly access database, handles group
 * table
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Page<Group> findGroupsByUserOwnerUserID(Integer userOwnerID, Pageable pageable);

}
