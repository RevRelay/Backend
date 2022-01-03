package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Layer for Pages
 * 
 * Interface using JpaRepository to access the database
 */
@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    /**
     * @param userOwnerID id to search by
     * @return Page page associated with userOwnerID
     */
    Page getPageByUserOwnerUserID(Integer userOwnerID);

    /**
     * @param groupID id to search by
     * @return Page page associated with groupID
     */
    Page getPageByGroupOwnerGroupID(Integer groupID);
}
