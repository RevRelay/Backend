package com.revature.RevRelay.models.dtos;

import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
