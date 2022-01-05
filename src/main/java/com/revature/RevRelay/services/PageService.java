package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.PageRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@NoArgsConstructor
public class PageService {
    PageRepository pageRepository;

    /**
     * @param pageRepository to be autowired
     */
    @Autowired
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    // CREATE

    /**
     * @param page page to be created
     * @return Page the page that is being created
     */
    public Page createPage(Page page) {
        return pageRepository.save(page);
    }

    // READ

    /**
     * @return admin method to get all pages that exist
     */
    public List<Page> getAll() {
        return pageRepository.findAll();
    }

    /**
     * @param userOwnerID the userOwnerID to find the page of
     * @return Page the associated Page with the provided userOwnerID
     */
    public Page getPageByUserOwnerUserID(Integer userOwnerID) {
        return pageRepository.getPageByUserOwnerUserID(userOwnerID);
    }

    /**
     * @param groupID the groupID to find the page of
     * @return Page the associated Page with the provided groupID
     */
    public Page getPageByGroupID(Integer groupID) {
        return pageRepository.getPageByGroupOwnerGroupID(groupID);
    }

    /**
     * @param pageID the pageID to find the page of
     * @return Page the associated Page with the provided pageID
     */
    public Page getPageByPageID(Integer pageID) throws Exception {
        return pageRepository.findById(pageID).orElseThrow(() -> new Exception("No Page Found"));
    }

    // UPDATE

    /**
     * @param page the page to be updated
     * @return Page the page that was updated with new values
     */
    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }

    // DELETE

    /**
     * @param pageID the pageID to delete from the database
     */
    public void deletePageByID(Integer pageID) {
        pageRepository.deleteById(pageID);
    }
}
