package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.Chatroom;
import com.revature.RevRelay.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Interface that uses JpaRepository to directly access database, handles group
 * table
 */
@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
	Page<Chatroom> findAllByMembersUserID(int userID,Pageable pageable);
}
