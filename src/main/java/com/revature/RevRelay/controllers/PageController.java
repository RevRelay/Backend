package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.services.PageService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles pages CRUD functionality relating to page model
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


    /**
     * @param page to be created
     * @return ResponseEntity<Page> Returns the created Page is successful
     */
    @PostMapping
    public ResponseEntity<Page> createPage(@RequestBody Page page) {
        return ResponseEntity.ok(pageService.createPage(page));
    }

    /**
     * @return List<Page> of all pages
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
    public Page getPageByUserOwnerUserID(@PathVariable Integer userOwnerID) {
        return pageService.getPageByUserOwnerUserID(userOwnerID);
    }

    /**
     * @param groupID to be searched and returned
     * @return Page single page matching provided groupID
     */
    @GetMapping("/groups/{groupID}")
    public Page getPageByGroupOwnerID(@PathVariable Integer groupID) {
        return pageService.getPageByGroupID(groupID);
    }

    /**
     * @param Username to be searched and returned
     * @return Page single page matching provided pageID
     */
    @GetMapping("/friends/{username}")
    public ResponseEntity<?> getAllFriends(@PathVariable String username) throws Exception {
            return ResponseEntity.ok(pageService.getAllFriendsFromUser(username));
    }
    /**
     * @param pageID pageID to be searched and returned
     * @return Page single page matching provided pageID
     */
    @GetMapping("/{pageID}")
    public ResponseEntity<?> getPageByPageID(@PathVariable Integer pageID) {
        try {
            return ResponseEntity.ok(pageService.getPageByPageID(pageID));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * @param page the page to be updated
     * @return Page single page with the updated values
     */
    @PutMapping
    public Page updatePage(@RequestBody Page page) {
        return pageService.updatePage(page);
    }

    /**
     * @param pageID the pageID to be deleted from database
     */
    @DeleteMapping("/{pageID}")
    public void deletePageByID(@PathVariable Integer pageID) {
        pageService.deletePageByID(pageID);
    }
}
