package com.revature.RevRelay.models.dtos;

import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * ONLY USE FOR USER UPDATE FROM FRONTEND, WILL BREAK OTHERWISE
 * YOU HAVE BEEN WARNED
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String displayName;

    public UserUpdateDTO(User user){
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        birthDate = user.getBirthDate().toString();
        displayName = user.getDisplayName();
    }
}
