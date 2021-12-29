package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.repositories.GroupRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service layer for Group model
 * 
 * @author Isaiah Anason
 * @author Noah Frederick
 * @author Loustauanau Luis
 * @author Evan Ritchey
 * @author Ryan Haynes
 */
@NoArgsConstructor
@Service
public class GroupService {

    GroupRepository groupRepository;

    /**
     * Constructor for GroupService
     * 
     * @param groupRepository is the data layer for the Group model
     */
    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Persists a new Group to the database
     * 
     * Receives a Group object from the controller and persists it as a new Group
     * record on the database
     * 
     * @param group is the Group object to be persisted
     * @return Group is the Group created on the database
     */
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }//tested

    /**
     * Retrieves all Groups from the database
     * 
     * @return Page<Group> is a page containing all the Groups
     */
    public Page<Group> getAll() {
        return groupRepository.findAll(Pageable.unpaged());
    }

    /**
     * Retrieves all Groups from the database
     * 
     * Receives a pageable, that specifies a specific page to return from
     * Page<Group>
     * 
     * @param pageable is the specific page to return from the Page<Group>
     * @return Page<Group> is page containing the Groups from a specific page
     */
    public Page<Group> getAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    /**
     * Retrieves all Groups from the database by their OwnerID
     * 
     * Receives a OwnerID from the controller and queries the database for
     * every Group which has that OwnerID
     * 
     * @param userOwnerID is the ID of the owner used when querying the database
     * @return Page<Group> is page containing the Groups
     */
    public Page<Group> findAllByUserOwnerID(Integer userOwnerID) {
        return groupRepository.findGroupsByUserOwnerID(userOwnerID, Pageable.unpaged());
    }

    /**
     * Retrieves all Groups from the database by their OwnerID
     * 
     * Receives a OwnerID from the controller and queries the database for
     * every Group which has that OwnerID. Also receives a pageable, that specifies
     * a specific page to return from Page<Group>
     * 
     * @param userOwnerID is the ID of the owner used when querying the database
     * @param pageable    is the specific page to return from the Page<Group>
     * @return Page<Group> is page containing the Groups from a specific page
     */
    public Page<Group> findAllByUserOwnerID(Integer userOwnerID, Pageable pageable) {
        return groupRepository.findGroupsByUserOwnerID(userOwnerID, pageable);
    }

    /**
     * Retrieves all Groups from the database by their ID
     * 
     * Receives a Group ID from the controller and queries the database for
     * that group
     * 
     * @param groupID
     * @return Group
     */
    public Group getGroupByGroupID(Integer groupID) {
        return groupRepository.getGroupsByGroupID(groupID).orElse(null);
        // TODO Custom exception
    }

    /**
     * Updates a Group
     * 
     * Receives a Group object from the controller and updates that Group on the
     * database
     * 
     * @param group is the Group object to be persisted
     * @return Group is the Group created on the database
     */
    public Group updateGroups(Group group) {
        return groupRepository.save(group);
    }

    /**
     * Deletes a Group
     * 
     * Receives a Group ID from the controller and deletes the Group with that ID
     * from the database
     * 
     * @param groupID
     */
    public void deleteGroupsByID(Integer groupID) {
        groupRepository.deleteById(groupID);
    }
}
