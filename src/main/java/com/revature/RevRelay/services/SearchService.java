package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.SearchResultItem;
import com.revature.RevRelay.repositories.GroupRepository;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class providing search results that require querying multiple data sets (i.e. Users and Group).
 *
 * @author Nathan Luther
 */
@Service
public class SearchService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PageRepository pageRepository;

    @Autowired
    public SearchService(UserRepository userRepository, GroupRepository groupRepository, PageRepository pageRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.pageRepository = pageRepository;
    }

    /**
     * Returns the full list of groups and users with names matching a search term.
     *
     * @param searchTerm String to be compared to User DisplayNames and Group GroupNames.
     * @return An ArrayList containing the full set of matching Users and Groups,
     * formatted with a DTO containing ID, type (user vs group) and corresponding page ID.
     */
    public List<SearchResultItem> SearchAllByName(String searchTerm) {
        List<User> matchingUsers = userRepository.findByDisplayNameContainingIgnoreCase(searchTerm).orElse(null);
        List<Group> matchingGroups = groupRepository.findByGroupNameContainingIgnoreCase(searchTerm).orElse(null);
        List<SearchResultItem> searchResults = new ArrayList<>();
        if (matchingUsers != null) {
            for (User user : matchingUsers) {
                SearchResultItem resultItem = new SearchResultItem(user);
                resultItem.setPageId(pageRepository.getPageByUserOwnerUserID(user.getUserID()).getPageID());
                searchResults.add(resultItem);
            }
        }
        if (matchingGroups != null) {
            for (Group group : matchingGroups) {
                SearchResultItem resultItem = new SearchResultItem(group);
                resultItem.setPageId(pageRepository.getPageByGroupOwnerGroupID(group.getGroupID()).getPageID());
                searchResults.add(resultItem);
            }
        }
        return searchResults;
    }

    /**
     * Returns a list of groups and users with names matching a search term with size defined by a pageable.
     *
     * @param searchTerm String to be compared to User DisplayNames and Group GroupNames.
     * @param pageable   Pageable for limiting the size of the search result set.
     * @return An ArrayList containing the full set of matching Users and Groups,
     * formatted with a DTO containing ID, type (user vs group) and corresponding page ID.
     */
    public List<SearchResultItem> SearchAllByName(String searchTerm, Pageable pageable) {
        Page<User> matchingUsers = userRepository.findByDisplayNameContainingIgnoreCase(searchTerm, pageable);
        Page<Group> matchingGroups = groupRepository.findByGroupNameContainingIgnoreCase(searchTerm, pageable);
        List<SearchResultItem> searchResults = new ArrayList<>();
        if (!matchingUsers.isEmpty()) {
            for (User user : matchingUsers) {
                SearchResultItem resultItem = new SearchResultItem(user);
                resultItem.setPageId(pageRepository.getPageByUserOwnerUserID(user.getUserID()).getPageID());
                searchResults.add(resultItem);
            }
        }
        if (!matchingGroups.isEmpty()) {
            for (Group group : matchingGroups) {
                SearchResultItem resultItem = new SearchResultItem(group);
                resultItem.setPageId(pageRepository.getPageByGroupOwnerGroupID(group.getGroupID()).getPageID());
                searchResults.add(resultItem);
            }
        }
        return searchResults;
    }
}
