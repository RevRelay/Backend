package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.services.GroupService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller endpoints for Group model
 * 
 * @author Archbishop: Isaiah Anason (Lead Devops Engineer)
 * @author Bishop: Noah Frederick (Team Lead)
 * @author Bishop: Loustauanau Luis (Lead Devops Engineer)
 * @author Bishop: Evan Ritchey (Lead QA and Back-End Engineer)
 * @author Priest: Ryan Haynes (Intern Devops Engineer)
 * @version 1.17
 */
@NoArgsConstructor
@RestController
@RequestMapping(value = "/groups")
public class GroupsController {
    GroupService groupService;

    /**
     * Constructor for GroupsController
     * 
     * @param groupService is the service layer for Group
     */
    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Create Endpoints

    /**
     * Endpoint for persisting a Group onto the database
     * 
     * Receives JSON and maps it into a new Group object. The Group object is passed
     * to the service layer to be persisted
     * 
     * @param group is the Group object mapped from JSON
     * @return ResponseEntity<?> contains the response and the newly create group
     */
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    // Read Endpoints

    /**
     * Endpoint for retrieving Groups from database by their owner
     * 
     * Takes in a path variable, userOwnerID, and queries the database for every
     * group which is owned by that userID
     * 
     * @param userOwnerID is the ID of the owner to query the database for
     * @return Page<Group> is a pageable containing all the results of the query. It
     *         isTo
     *         be parsed on the front-end
     */
    @GetMapping("/all/{userOwnerID}")
    public Page<Group> getGroupByUserOwnerID(@PathVariable Integer userOwnerID) {
        return groupService.findAllByUserOwnerID(userOwnerID);
    }

    /**
     * @return Page<Group>
     */
    @GetMapping("/all")
    public Page<Group> getAll() {
        return groupService.getAll();
    }

    /**
     * @param groupID
     * @return Group
     */
    @GetMapping("/{groupsID}")
    public Group getGroupsByGroupID(@PathVariable Integer groupID) {
        return groupService.getGroupByGroupID(groupID);
    }

    // Update Endpoints

    /**
     * @param group
     * @return Group
     */
    @PutMapping
    public Group updateGroups(@RequestBody Group group) {
        return groupService.updateGroups(group);
    }

    // Delete Endpoints

    /**
     * @param groupID
     */
    @DeleteMapping("/{groupID}")
    public void deleteGroupsByID(@PathVariable Integer groupID) {
        groupService.deleteGroupsByID(groupID);
    }
}
