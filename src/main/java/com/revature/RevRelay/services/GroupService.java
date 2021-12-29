package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.repositories.GroupRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class GroupService {

    GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }

    //CREATE
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    //READ
    public Page<Group> getAll() {
        return groupRepository.findAll(Pageable.unpaged());
    }
    public Page<Group> getAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    public Page<Group> findAllByUserOwnerID(Integer userOwnerID, Pageable pageable){
        return groupRepository.findGroupsByUserOwnerID(userOwnerID, pageable);
    }
    public Page<Group> findAllByUserOwnerID(Integer userOwnerID){
        return groupRepository.findGroupsByUserOwnerID(userOwnerID,Pageable.unpaged());
    }

    public Group getGroupByGroupID(Integer groupID) {
        return groupRepository.getGroupsByGroupID(groupID).orElse(null);
        //TODO Custom exception
    }

    //UPDATE
    public Group updateGroups(Group group) {
        return groupRepository.save(group);
    }

    //DELETE
    public void deleteGroupsByID(Integer groupID) {
        groupRepository.deleteById(groupID);
    }
}
