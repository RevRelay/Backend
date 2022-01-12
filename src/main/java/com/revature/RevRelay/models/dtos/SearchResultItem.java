package com.revature.RevRelay.models.dtos;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Search Result Item Model
 *
 * For a Page gives only the information needed when searching results.
 * This includes the search result id, the type being either if it is a user or a group,
 * the display name of the page, and the pageID.
 */
@Getter
@Setter
@AllArgsConstructor
public class SearchResultItem {
    private int id;
    private SearchResultItemTypes type;
    private String name;
    private int pageId;

    public SearchResultItem(User user) {
        this.type = SearchResultItemTypes.USER;
        this.id = user.getUserID();
        this.name = user.getDisplayName();
    }

    public SearchResultItem(Group group) {
        this.type = SearchResultItemTypes.GROUP;
        this.id = group.getGroupID();
        this.name = group.getGroupName();
    }

    public enum SearchResultItemTypes {
        USER,GROUP
    }
}
