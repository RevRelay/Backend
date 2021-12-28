package com.revature.RevRelay.services;

import com.revature.RevRelay.persistence.UserRepository;
import com.revature.RevRelay.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.omg.CosNaming.NamingContextPackage.NotFound;


@Service // this annotation is to denote that this is a service class for user
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Saves the user to the database.
     * @param user the user to be saved.
     * @return the User, but not saved to the database.
     */
    public User createNewUser(User user)
    {
        return userRepository.save(user);
    }

    /**
     * Gets a list of all users.
     * @return List<User> for all users in the database ordered by UserID in descending order.
     */
    public Page<User> findAllUsers(Pageable pageable)
    {
        return userRepository.findAllByOrderBydisplayName(pageable);
    }

    /**
     *Logs in the user with the given username and password, then returns that User.
     * @param username the username to match.
     * @param password the password to match.
     * @return User of the given username AND password.
     * @throws AccessDeniedException
     */
    public User login(String username, String password) throws AccessDeniedException
    {
        try
        {
            User user = findUserByUsername(username);
            if (user.getPassword().equals(password)) return user;
        }
        catch (NotFound e)
        {
            throw new AccessDeniedException("Incorrect username/password");
        }
        throw new AccessDeniedException("Incorrect username/password");
    }

    /**
     * Update password by the provided username.
     * @param username the username that already exists on the repository.
     * @param oldPassword the password that the user currently uses.
     * @param newPassword the password that the user wants to switch to.
     * Get the current username from the user.
     * If the username is already in the database, then we can update the password
     */
    public boolean updatePassword(String username, String oldPassword, String newPassword)
    {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user != null)
        {
            if(user.getPassword().equals(oldPassword))
            {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
            else
            {
                // Username/Password invalid.
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Get the user with the given username.
     * @param username the username to match.
     * @return User with the given username.
     * @throws NotFound if the username is not in the database this will be thrown.
     */
    public User findUserByUsername(String username) throws NotFound
    {
        return userRepository.findByUsername(username).orElseThrow(NotFound::new);
    }

    /**
     * Get the user with the given user ID.
     * @param userID the user ID to match.
     * @return User with the given userID.
     * @throws NotFound if the userID is not in the database this will be thrown.
     */
    public User findUserByUserID(int userID) throws NotFound
    {
        return userRepository.findByUserID(userID).orElseThrow(NotFound::new);
    }

    /**
     * Updating the last name of the user
     * @param userID the user to be matched by userID
     * @param email the email to be changed to.
     * @return boolean if the userId exits then change the last name of the user
     * Get the userId from the employee.
     * If the last name is already in the database, then update the last name
     */
    public boolean updateEmail(int userID, String email)
    {
        User user = userRepository.findByUserID(userID).orElse(null);
        if(user != null)
        {
            user.setEmail(email);
            userRepository.save(user);
            return true;
        }
        else
        {
            return false;
        }
    }
}
