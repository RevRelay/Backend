package com.revature.RevRelay.repository;

import com.revature.RevRelay.models.Groups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupsRepository extends JpaRepository<Groups,Integer> {
    Page<Groups> findGroupsByUserOwnerID(Integer userOwnerID, Pageable pageable);


    Optional<Groups> getGroupsByGroupID(Integer groupID);
}
