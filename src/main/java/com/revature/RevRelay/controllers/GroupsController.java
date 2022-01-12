package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.services.GroupService;
import com.revature.RevRelay.services.UserService;
import com.revature.RevRelay.utils.JwtUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
     * Constructor for the GroupsController.
     *
     * @param groupService The service layer for Groups.
     * @param userService The service layer for Users.
     * @param jwtUtil ---
     */
    @Autowired
    public GroupsController(GroupService groupService, UserService userService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * Endpoint for persisting a Group onto the database.
     * 
     * Receives JSON and maps it into a new Group object if there is a token of the right form.
     * The Group object is passed to the service layer to be persisted.
     * 
     * @param group The Group object mapped from JSON
     * @return ResponseEntity<?> contains the response and the newly create group.
     */
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Group group, @RequestHeader("Authorization") String token) {
        if (token != null && token.length() > 1) {
            group.setUserOwner(userService.loadUserByToken(token.replace("Bearer", "").trim()));
        }
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    /**
     * Endpoint for retrieving Groups from database by their owner.
     * 
     * Takes in a path variable, userOwnerID, and queries the database for every
     * group which is owned by that userID.
     * 
     * @param userOwnerID The userID of the owner to query the database for.
     * @return Page<Group> A pageable containing all the results of the query.
     */
    @GetMapping("/all/{userOwnerID}")
    public Page<Group> getGroupByUserOwnerID(@PathVariable Integer userOwnerID) {
        return groupService.findAllByUserOwnerID(userOwnerID);
    }

    /**
     * Endpoint for retrieving all groups
     *
     * @return Page<Group> A pageable containing all the groups.
     */
    @GetMapping("/all")
    public Page<Group> getAll() {
        return groupService.getAll();
    }

    /**
     * Endpoint of retrieving a group using its groupID.
     *
     * @param groupID The groupID corresponding to group being retrieved.
     * @return The group with the given groupID.
     */
    @GetMapping("/{groupID}")
    public Group getGroupsByGroupID(@PathVariable Integer groupID) {
        return groupService.getGroupByGroupID(groupID);
    }

    /**
     * Endpoint for updating a group with a new group object.
     *
     * @param group The new group object being updated.
     * @return Group with the upated info updated.
     */
    @PutMapping
    public Group updateGroups(@RequestBody Group group) {
        return groupService.updateGroups(group);
    }

    /**
     * Endpoint for deleting a group using its groupID.
     *
     * @param groupID The group's groupID that is to be deleted.
     */
    @DeleteMapping("/{groupID}")
    public void deleteGroupsByID(@PathVariable Integer groupID) {
        groupService.deleteGroupsByID(groupID);
    }

    /**
     * Endpoint for adding members to groups.
     *
     * @param groupID The groupID that a member is being added to.
     * @param userID The userID of the member being added to the group.
     * @return The group with the added member with the given userID.
     */
    @PostMapping("/addmember")
    public Group addMember(@RequestParam("GroupID") Integer groupID, @RequestParam("UserID") Integer userID) {
        System.out.println("***********************SOMETHING\n\n\n\n");
        return groupService.addMember(groupID, userID);
    }

    /**
     * Endpoint for removing members from groups by userID.
     *
     * @param groupID The groupID that a member is being removed from.
     * @param userID The userID of the member being removed from the group.
     * @return The group with the member with the given userID removed.
     */
    @DeleteMapping("/deletemember")
    public ResponseEntity<?> deleteMember(@RequestParam("GroupID") Integer groupID, @RequestParam("UserID") Integer userID) {
        groupService.deleteMember(groupID, userID);
        return ResponseEntity.ok(groupID);
    }

    /**
     * Gets all Groups that a user with a given userID is a part of (Owner or Member).
     *
     * @param userID The userID of the given user we are getting all groups of.
     * @param pageNumber pageable config of the current page number.
     * @param pageSize pageable config of the current page size aka the number of responses listed on the page.
     * @return ResponseEntity with a pageable with the requested page number and page size.
     *          If no page number and size if given it sets this to page 1 with a page size of 10.
     */
    @GetMapping("/getgroups/{userID}")
    public ResponseEntity<?> getMembers(@PathVariable Integer userID,@RequestParam(value = "pageNumber",required = false) Integer pageNumber,@RequestParam(value = "pageSize",required = false) Integer pageSize){
        if(pageNumber!=null && pageSize!=null)
            return ResponseEntity.ok(groupService.findAllMembersByUserID(userID, PageRequest.of(pageNumber,pageSize)));
        else return ResponseEntity.ok(groupService.findAllMembersByUserID(userID, Pageable.ofSize(10)));
    }
}
