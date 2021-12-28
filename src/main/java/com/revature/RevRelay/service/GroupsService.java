package com.revature.RevRelay.service;

import com.revature.RevRelay.models.Groups;
import com.revature.RevRelay.repository.GroupsRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class GroupsService {

    GroupsRepository groupsRepository;

    @Autowired
    public GroupsService(GroupsRepository groupsRepository){
        this.groupsRepository = groupsRepository;
    }

    //CREATE
    public Groups createGroup(Groups groups) {
        return groupsRepository.save(groups);
    }

    //READ
    public Page<Groups> getAll() {
        return groupsRepository.findAll(Pageable.unpaged());
    }
    public Page<Groups> getAll(Pageable pageable) {
        return groupsRepository.findAll(pageable);
    }

    public Page<Groups> findAllByUserOwnerID(Integer userOwnerID, Pageable pageable){
        return groupsRepository.findGroupsByUserOwnerID(userOwnerID, pageable);
    }
    public Page<Groups> findAllByUserOwnerID(Integer userOwnerID){
        return groupsRepository.findGroupsByUserOwnerID(userOwnerID,Pageable.unpaged());
    }

    public Groups getGroupByGroupID(Integer groupID) {
        return groupsRepository.getGroupsByGroupID(groupID).orElse(null);
        //TODO Custom exception
    }

    //UPDATE
    public Groups updateGroups(Groups groups) {
        return groupsRepository.save(groups);
    }

    //DELETE
    public void deleteGroupsByID(Integer groupID) {
        groupsRepository.deleteById(groupID);
    }
}
