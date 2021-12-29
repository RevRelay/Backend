package com.revature.RevRelay.services;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createNewUser(User user){return userRepository.save(user);}

    /*
    Updates a User's first name, to be displayed on their profile
     */
    public boolean updateFirstName(int userID, String firstName){
        User user = userRepository.findByUserID(userID).orElse(null);
        if(user != null){
            user.setFirstName(firstName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /*
    Updates a User's last name, to be displayed on their profile
     */
    public boolean updateLastName(int userID, String lastName){
        User user = userRepository.findByUserID(userID).orElse(null);
        if(user != null){
            user.setLastName(lastName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /*
    Attempts to update a User's password
    Takes in the old password, the desired new password, and another instance of the new password for confirmation
    If the old password is incorrect, or the new password does not match the confirmation, returns false
    If a User's password is successfully updated, save the User and return true
     */
    public boolean updatePassword(int userID, String oldPassword, String newPassword, String confirmPassword){
        User user = userRepository.findByUserID(userID).orElse(null);
        if(user != null){
            if((user.getPassword().equals(oldPassword)) && (newPassword.equals(confirmPassword))){
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    /*
    Updates a User's birthday
     */
    public boolean updateBirthDate(int userID, Date birthDate){
        User user = userRepository.findByUserID(userID).orElse(null);
        if(user != null){
            user.setBirthDate(birthDate);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /*
    Updates a User's display name, which will be seen by other Users in chat rooms/groups
     */
    public boolean updateDisplayName(int userID, String displayName){
        User user = userRepository.findByUserID(userID).orElse(null);
        if(user != null){
            user.setDisplayName(displayName);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
