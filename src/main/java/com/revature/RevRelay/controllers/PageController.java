package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.services.PageService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@NoArgsConstructor
@RestController
@RequestMapping(value = "/pages")
/**
 * This controller handles pages CRUD functionality
 */
public class PageController {
    PageService pageService;

    @Autowired
    /**
     * @param pageService autowired pageService
     */
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    //CREATE
    @PostMapping
    /**
     * @param Page page to be created
     * @return ResponseEntity<Page> Returns the created Page is successful
     */
    public ResponseEntity<Page> createPage(@RequestBody Page page) {
        return ResponseEntity.ok(pageService.createPage(page));
    }

    //READ
    @GetMapping("/all")
    /**
     * @return List<Page>
     */
    public List<Page> getAll() {
        return pageService.getAll();
    }

    @GetMapping("/users/{userOwnerID}")
    /**
     * @param userOwnerID userOwnerID to be searched and returned
     * @return Page single page matching provided userOwnerID
     */
    public Page getPageByUserOwnerID(@PathVariable Integer userOwnerID) {
        return pageService.getPageByUserOwnerID(userOwnerID);
    }

    @GetMapping("/groups/{groupID}")
    /**
     * @param groupOwnerID groupOwnerID to be searched and returned
     * @return Page single page matching provided groupID
     */
    public Page getPageByGroupOwnerID(@PathVariable Integer groupID) {
        return pageService.getPageByGroupID(groupID);
    }

    @GetMapping("/{pageID}")
    /**
     * @param pageID pageID to be searched and returned
     * @return Page single page matching provided pageID
     */
    public Page getPageByPageID(@PathVariable Integer pageID) {
        return pageService.getPageByPageID(pageID);
    }

    //UPDATE
    @PutMapping
    /**
     * @param page the page to be updated
     * @return Page single page with the updated values
     */
    public Page updatePage(@RequestBody Page page) {
        return pageService.updatePage(page);
    }

    //DELETE
    @DeleteMapping("/{pageID}")
    /**
     * @param pageID the pageID to be deleted from database
     */
    public void deletePageByID(@PathVariable Integer pageID) {
        pageService.deletePageByID(pageID);
    }
}
