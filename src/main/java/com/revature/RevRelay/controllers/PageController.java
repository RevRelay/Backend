package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.services.PageService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * This controller handles pages CRUD functionality
 * 
 * @author Isaiah Anason
 * @author Noah Frederick
 * @author Loustauanau Luis
 * @author Evan Ritchey
 * @author Ryan Haynes
 */
@NoArgsConstructor
@RestController
@RequestMapping(value = "/pages")
public class PageController {
    PageService pageService;

    /**
     * @param pageService autowired pageService
     */
    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    // CREATE

    /**
     * @param Page page to be created
     * @return ResponseEntity<Page> Returns the created Page is successful
     */
    @PostMapping
    public ResponseEntity<Page> createPage(@RequestBody Page page) {
        return ResponseEntity.ok(pageService.createPage(page));
    }

    // READ

    /**
     * @return List<Page>
     */
    @GetMapping("/all")
    public List<Page> getAll() {
        return pageService.getAll();
    }

    /**
     * @param userOwnerID userOwnerID to be searched and returned
     * @return Page single page matching provided userOwnerID
     */
    @GetMapping("/users/{userOwnerID}")
    public Page getPageByUserOwnerID(@PathVariable Integer userOwnerID) {
        return pageService.getPageByUserOwnerID(userOwnerID);
    }

    /**
     * @param groupOwnerID groupOwnerID to be searched and returned
     * @return Page single page matching provided groupID
     */
    @GetMapping("/groups/{groupID}")
    public Page getPageByGroupOwnerID(@PathVariable Integer groupID) {
        return pageService.getPageByGroupID(groupID);
    }

    /**
     * @param pageID pageID to be searched and returned
     * @return Page single page matching provided pageID
     */
    @GetMapping("/{pageID}")
    public Page getPageByPageID(@PathVariable Integer pageID) {
        return pageService.getPageByPageID(pageID);
    }

    // UPDATE
    /**
     * @param page the page to be updated
     * @return Page single page with the updated values
     */
    @PutMapping
    public Page updatePage(@RequestBody Page page) {
        return pageService.updatePage(page);
    }

    // DELETE
    /**
     * @param pageID the pageID to be deleted from database
     */
    @DeleteMapping("/{pageID}")
    public void deletePageByID(@PathVariable Integer pageID) {
        pageService.deletePageByID(pageID);
    }
}
