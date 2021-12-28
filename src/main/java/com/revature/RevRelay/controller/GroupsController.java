package com.revature.RevRelay.controller;

import com.revature.RevRelay.models.Groups;
import com.revature.RevRelay.service.GroupsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@NoArgsConstructor
@RestController
@RequestMapping(value = "/groups")
public class GroupsController {
    GroupsService groupsService;

    @Autowired
    public GroupsController(GroupsService groupsService) {
        this.groupsService = groupsService;
    }

    @GetMapping
    public Groups test() {
        return new Groups();
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Groups groups) {
        return ResponseEntity.ok(groupsService.createGroup(groups));
    }

    //READ
    @GetMapping("/all/{userOwnerID}")
    public Page<Groups> getGroupByUserOwnerID(@PathVariable Integer userOwnerID) {
        return groupsService.findAllByUserOwnerID(userOwnerID);
    }

    @GetMapping("/all")
    public Page<Groups> getAll() {
        return groupsService.getAll();
    }

    @GetMapping("/{groupsID}")
    public Groups getGroupsByGroupID(@PathVariable Integer groupID) {
        return groupsService.getGroupByGroupID(groupID);
    }

    //UPDATE
    @PutMapping
    public Groups updateGroups(@RequestBody Groups groups) {
        return groupsService.updateGroups(groups);
    }

    //DELETE
    @DeleteMapping("/{groupID}")
    public void deleteGroupsByID(@PathVariable Integer groupID) {
        groupsService.deleteGroupsByID(groupID);
    }
}
