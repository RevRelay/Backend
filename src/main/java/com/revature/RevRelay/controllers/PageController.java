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
     * Constructor for the GroupsController.
     *
     * @param pageService The service layer for Pages.
     */
    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }


    /**
     * Creates a page.
     *
     * @param page The information for a page to be created
     * @return ResponseEntity<Page> Returns the created Page.
     */
    @PostMapping
    public ResponseEntity<Page> createPage(@RequestBody Page page) {
        return ResponseEntity.ok(pageService.createPage(page));
    }

    /**
     * Gets a List of all Pages.
     *
     * @return List<Page> List of all Pages.
     */
    @GetMapping("/all")
    public List<Page> getAll() {
        return pageService.getAll();
    }

    /**
     * Gets the page owned by a user by userOwnerID.
     *
     * @param userOwnerID The user that owns the Page by userOwnerID.
     * @return A single Page owned by a user with the matching the provided userOwnerID.
     */
    @GetMapping("/users/{userOwnerID}")
    public Page getPageByUserOwnerUserID(@PathVariable Integer userOwnerID) {
        return pageService.getPageByUserOwnerUserID(userOwnerID);
    }

    /**
     * Gets the page owned by a group by groupID.
     *
     * @param groupID The group that owns the Page by groupID.
     * @return A single Page owned by a group with the matching provided groupID.
     */
    @GetMapping("/groups/{groupID}")
    public Page getPageByGroupOwnerID(@PathVariable Integer groupID) {
        return pageService.getPageByGroupID(groupID);
    }

    /**
     * Gets the Set of all the friends of a user by username.
     *
     * @param username The username of the user we are getting the friends of.
     * @return Set of all the friends of the user by username.
     */
    @GetMapping("/friends/{username}")
    public ResponseEntity<?> getAllFriends(@PathVariable String username) throws Exception {
            return ResponseEntity.ok(pageService.getAllFriendsFromUser(username));
    }
    
    /**
     * Gets a Page by its pageID.
     *
     * @param pageID The pageID of the page what is wanted.
     * @return A single Page with the matching pageID provided.
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
     * Updates a Page with the given page information.
     *
     * @param page The page with the information to be updated to.
     * @return A single Page with the updated values.
     */
    @PutMapping
    public ResponseEntity<?> updatePage(@RequestBody PageDTO page) {
        try{
			return ResponseEntity.ok(pageService.updateFromDTO(page));
		} catch (Exception e){
			return ResponseEntity.notFound().build();
		}

    }

    /**
     * Deletes a Page with the given pageID.
     *
     * @param pageID The pageID of the page to be deleted from the database.
     */
    @DeleteMapping("/{pageID}")
    public void deletePageByID(@PathVariable Integer pageID) {
        pageService.deletePageByID(pageID);
    }
}
