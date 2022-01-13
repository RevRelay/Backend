package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.dtos.PageDTO;
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
    public ResponseEntity<Page> createPage(@RequestHeader("Authorization") String token,@RequestBody Page page) {
        return ResponseEntity.ok(pageService.createPage(page));
    }

    /**
     * @return List<Page> of all pages
     */
    @GetMapping("/all")
    public List<Page> getAll(@RequestHeader("Authorization") String token) {
        return pageService.getAll();
    }

    /**
     * @param userOwnerID userOwnerID to be searched and returned
     * @return Page single page matching provided userOwnerID
     */
    @GetMapping("/users/{userOwnerID}")
    public Page getPageByUserOwnerUserID(@RequestHeader("Authorization") String token,@PathVariable Integer userOwnerID) {
        return pageService.getPageByUserOwnerUserID(userOwnerID);
    }

    /**
     * @param groupID to be searched and returned
     * @return Page single page matching provided groupID
     */
    @GetMapping("/groups/{groupID}")
    public Page getPageByGroupOwnerID(@RequestHeader("Authorization") String token,@PathVariable Integer groupID) {
        return pageService.getPageByGroupID(groupID);
    }

    /**
     * @param Username to be searched and returned
     * @return Page single page matching provided pageID
     */
    @GetMapping("/friends/{username}")
    public ResponseEntity<?> getAllFriends(@RequestHeader("Authorization") String token,@PathVariable String username) throws Exception {
            return ResponseEntity.ok(pageService.getAllFriendsFromUser(username));
    }
    
    /**
     * @param pageID pageID to be searched and returned
     * @return Page single page matching provided pageID
     */
    @GetMapping("/{pageID}")
    public ResponseEntity<?> getPageByPageID(@RequestHeader("Authorization") String token,@PathVariable Integer pageID) {
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
    public ResponseEntity<?>  updatePage(@RequestHeader("Authorization") String token,@RequestBody PageDTO page) {
        try{
			return ResponseEntity.ok(pageService.updateFromDTO(page));
		} catch (Exception e){
			return ResponseEntity.notFound().build();
		}

    }

    /**
     * @param pageID the pageID to be deleted from database
     */
    @DeleteMapping("/{pageID}")
    public void deletePageByID(@RequestHeader("Authorization") String token,@PathVariable Integer pageID) {
        pageService.deletePageByID(pageID);
    }
}
