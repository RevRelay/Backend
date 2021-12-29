package com.revature.RevRelay.repositories;

import com.revature.RevRelay.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    Page getPageByUserOwnerID(Integer userOwnerID);

    Page getPageByGroupOwnerID(Integer groupOwnerID);

    Page getPageByPageID(Integer pageID);
}
