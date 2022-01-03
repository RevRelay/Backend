package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * This Repo handles the other methods needed for full page CRUD functionality
 * not already defined in Spring JPA
 */
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
