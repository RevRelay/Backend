package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.GroupRepository;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

	PageRepository pageRepository;

    UserRepository userRepository;

    /**
     * Constructor for GroupService
     * 
     * @param groupRepository is the data layer for the Group model
     */
    @Autowired
    public GroupService(GroupRepository groupRepository,UserRepository userRepository,PageRepository pageRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
		this.pageRepository = pageRepository;
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
        com.revature.RevRelay.models.Page p = new com.revature.RevRelay.models.Page();
        p.setDescription("Empty Description");
        p.setBannerURL("https://i.imgur.com/jFU9RkE.jpeg");
        p.setPrivate(true);
        p.setGroupPage(true);
		group = groupRepository.save(group);
		addMember(group.getGroupID(),group.getUserOwnerID());
        p.setGroupOwner(group);
        group.setGroupPage(pageRepository.save(p));
        return groupRepository.save(group);
    }

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
        return groupRepository.findById(groupID).orElse(null);
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
     * @param groupID is the ID of the group to remove
     */
    public void deleteGroupsByID(Integer groupID) {
        groupRepository.deleteById(groupID);
    }

    /**
     *
     * @param groupID groupID to add a member to
     * @param userID the id of user to add to group
     */
    public Group addMember(Integer groupID, Integer userID) {
        Group group = groupRepository.findById(groupID).orElse(null);
        User user = userRepository.findById(userID).orElse(null);
        assert(group!=null && user!=null);
        List<Group> userGroups = user.getUserGroups();
        userGroups.add(group);
        user.setUserGroups(userGroups);
        userRepository.save(user);
        return groupRepository.findById(groupID).orElse(null);
    }

    /**
     *
     * @param groupID groupID to remove member from
     * @param userID loops through groups members and deletes userID
     */
    public void deleteMember(Integer groupID, Integer userID) {
        Group group = groupRepository.findById(groupID).orElse(null);
        User user = userRepository.findById(userID).orElse(null);
        assert(group!=null && user!=null);
        List<Group> userGroups = user.getUserGroups();
        user.setUserGroups(userGroups.stream()
                .filter((userGroup) ->
                        userGroup.getGroupID() != group.getGroupID()).collect(Collectors.toList()));
        userRepository.save(user);
    }

    /**
     * Finds all Groups a userID is associated with. Default to dump everything in 1 page
     * @param userID
     * @return
     */
    public Page<Group> findAllMembersByUserID(Integer userID){
        return groupRepository.findAllGroupByMembersUserIDOrUserOwnerID(userID,userID,Pageable.unpaged());
    }
    /**
     * Finds all Groups a userID is associated with. Config the pageable
     *
     * @param userID
     * @return
     */
    public Page<Group> findAllMembersByUserID(Integer userID,Pageable pageable){
        return groupRepository.findAllGroupByMembersUserIDOrUserOwnerID(userID,userID, pageable);
    }
}
