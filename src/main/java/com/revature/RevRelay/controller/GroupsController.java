package com.revature.RevRelay.controller;

import com.revature.RevRelay.models.Groups;
import com.revature.RevRelay.service.GroupsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.acl.Group;

@NoArgsConstructor
@RestController
@RequestMapping(value="/groups")
public class GroupsController {
    GroupsService groupsService;

    @Autowired
    public GroupsController(GroupsService groupsService){
        this.groupsService = groupsService;
    }


    //CREATE
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Groups groups){
        return ResponseEntity.ok(groupsService.createGroup(groups));
    }
    //READ
    @GetMapping("/all/{userOwnerID}")
    public Page<Groups> getGroupByUserId(@PathVariable Integer userOwnerID){
        return groupsService.findAllByUserId(userOwnerID);
    }
    @GetMapping("/all")
    public Page<Groups> getAll(){
        return groupsService.getAll();
    }
    @GetMapping("/{groupsId}")
    public Groups getGroupsByGroupID(@PathVariable Integer groupsID){
        return groupsService.getGroupByGroupId(groupsID);
    }
    //UPDATE
    @PutMapping
    public Groups updateGroups(@RequestBody Groups groups){
        return groupsService.updateGroups(groups);
    }

    //DELETE
    @DeleteMapping("{groupID}")
    public void deleteGroupsById(@PathVariable Integer groupID){
        groupsService.deleteGroupsById(groupID);
    }
}
