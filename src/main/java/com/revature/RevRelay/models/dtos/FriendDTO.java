package com.revature.RevRelay.models.dtos;

import com.revature.RevRelay.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FriendDTO Model
 *
 * A user that only contains the display name and userID of that user.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {
    private String displayName;
    private int userID;

    public FriendDTO(User user){
        displayName = user.getDisplayName();
        userID = user.getUserID();
    }
}
