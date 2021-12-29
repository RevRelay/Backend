package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.services.GroupService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@NoArgsConstructor
@RestController
@RequestMapping(value = "/groups")
public class GroupsController {
    GroupService groupService;

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public Group test() {
        return new Group();
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    //READ
    @GetMapping("/all/{userOwnerID}")
    public Page<Group> getGroupByUserOwnerID(@PathVariable Integer userOwnerID) {
        return groupService.findAllByUserOwnerID(userOwnerID);
    }

    @GetMapping("/all")
    public Page<Group> getAll() {
        return groupService.getAll();
    }

    @GetMapping("/{groupsID}")
    public Group getGroupsByGroupID(@PathVariable Integer groupID) {
        return groupService.getGroupByGroupID(groupID);
    }

    //UPDATE
    @PutMapping
    public Group updateGroups(@RequestBody Group group) {
        return groupService.updateGroups(group);
    }

    //DELETE
    @DeleteMapping("/{groupID}")
    public void deleteGroupsByID(@PathVariable Integer groupID) {
        groupService.deleteGroupsByID(groupID);
    }
}
