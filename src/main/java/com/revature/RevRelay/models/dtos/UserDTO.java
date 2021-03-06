package com.revature.RevRelay.models.dtos;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

/**
 * UserDTO model
 *
 * Includes almost all param for a user.Used so that password is not sent to the front end.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String displayName;
	private Page userPage;
    private int userID;

    public UserDTO(User user){
        username = user.getUsername();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        birthDate = user.getBirthDate();
        displayName = user.getDisplayName();
		userPage = user.getUserPage();
        userID = user.getUserID();
    }
}
