package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.services.GroupService;
import com.revature.RevRelay.services.UserService;
import com.revature.RevRelay.utils.JwtUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
TODO: refactor method returns to responseEntity?
TODO: refactor method returns to handle optionals?
 */
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

    UserService userService;

    JwtUtil jwtUtil;

    /**
     * Constructor for GroupsController
     * 
     * @param groupService is the service layer for Group
     */
    @Autowired
    public GroupsController(GroupService groupService, UserService userService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.groupService = groupService;
    }

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
    public ResponseEntity<?> createGroup(@RequestBody Group group, @RequestHeader("Authorization") String token) {
        if (token != null && token.length() > 1) {
            group.setUserOwner(userService.loadUserByUsername(jwtUtil.extractUsername(token.substring(7))));
        }
        return ResponseEntity.ok(groupService.createGroup(group));
    }

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
     * Endpoint for retrieving all group
     *
     * @return Page<Group> all the pages of a group
     */
    @GetMapping("/all")
    public Page<Group> getAll() {
        return groupService.getAll();
    }

    /**
     * Endpoint of retrieving a group using the groupID
     *
     * @param groupID id corresponding to group being retrieved
     * @return Group
     */
    @GetMapping("/{groupID}")
    public Group getGroupsByGroupID(@PathVariable Integer groupID) {
        return groupService.getGroupByGroupID(groupID);
    }

    /**
     * Endpoint for updating a group with a new group object
     *
     * @param group new group object being updated
     * @return Group that is updated
     */
    @PutMapping
    public Group updateGroups(@RequestBody Group group) {
        return groupService.updateGroups(group);
    }

    /**
     * Endpoint for deleting a group using the groupID
     *
     * @param groupID
     */
    @DeleteMapping("/{groupID}")
    public void deleteGroupsByID(@PathVariable Integer groupID) {
        groupService.deleteGroupsByID(groupID);
    }

    /**
     * Endpoint for adding members to groups
     *
     * @param groupID
     * @param userID
     */
    @PostMapping("/addmember")
    public void addMember(@RequestHeader("GroupID") Integer groupID, @RequestHeader("UserID") Integer userID) {
        groupService.addMember(groupID, userID);
    }

    /**
     * Endpoint for removing members from groups
     *
     * @param groupID
     * @param userID
     */
    @DeleteMapping("deletemember")
    public void deleteMember(@RequestHeader("GroupID") Integer groupID, @RequestHeader("UserID") Integer userID) {
        groupService.deleteMember(groupID, userID);
    }
}
