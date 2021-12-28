package com.revature.RevRelay.controller;

import com.revature.RevRelay.repository.GroupRepository;
import com.revature.RevRelay.service.GroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class GroupController {
    GroupService groupService;

    public GroupController(GroupService groupService){
        this.groupService = groupService;
    }
}
