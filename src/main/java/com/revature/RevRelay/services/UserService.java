package com.revature.RevRelay.services;

import com.revature.RevRelay.persistence.UserRepository;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserLoginAuthRequest;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.persistence.UserRepository;
import com.revature.RevRelay.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
//return to this later - NL
import org.omg.CosNaming.NamingContextPackage.NotFound;
  
@Service
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
  
    public User createUser(User user) {
        return createUser(new UserLoginAuthRequest(user.getUsername(),user.getPassword()));
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
            User user = loadUserByUsername(username);
            if (user.getPassword().equals(password)) return user;
        }
        catch (Exception e)
        {
            throw new AccessDeniedException("Incorrect username/password");
        }
        throw new AccessDeniedException("Incorrect username/password");
    }

    /**
     * Takes a user persists it then returns the user
     * @param userAuthRequest The Auth Request corresponding to the user that is going to be created
     * @return the full user object that was persisted is returned.
     */
    public User createUser(UserRegisterAuthRequest userAuthRequest) throws IllegalArgumentException {
        if (userRepository.existsByUsername(userAuthRequest.getUsername()) || userAuthRequest.getUsername() == null) {
            throw new IllegalArgumentException("Username Not Valid");
        }
        else {
            User user = new User();
            user.setDisplayName(userAuthRequest.getDisplayName());
            user.setEmail(userAuthRequest.getEmail());
            user.setUsername(userAuthRequest.getUsername());
            user.setPassword(userAuthRequest.getPassword());
            return userRepository.save(user);
        }
    }

    private User createUser(UserLoginAuthRequest userAuthRequest) throws IllegalArgumentException {
        if (userRepository.existsByUsername(userAuthRequest.getUsername()) || userAuthRequest.getUsername() == null) {
            throw new IllegalArgumentException("Username Not Valid");
        }
        else {
            User user = new User();
            user.setUsername(userAuthRequest.getUsername());
            user.setPassword(userAuthRequest.getPassword());
            return userRepository.save(user);
        }
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
  
     /** implementation of UserDetailsService method for Spring Security.
     *
     * @param username Username expected to be in database.
     * @return User object from database.
     * @throws UsernameNotFoundException Throws exception on empty optional from repository.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new UsernameNotFoundException("Username Not Found");
        }
    }

    public User findByToken(String token) throws Exception {
        Optional<User> user = userRepository.findByUsername(jwtUtil.extractUsername(token));
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new Exception("Token Does Not Correspond to User");
        }
    }
  
     /**
     * Gets a list of all users.
     * @return List<User> for all users in the database ordered by UserID in descending order.
     */
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAllOrderByDisplayName(pageable);
    }

    /**
     * Takes a user and returns a boolean if the username AND password matched what is in the database
     * @param user the User to check against the persisted database
     * @return True if the user is in the system, false otherwise
     * @throws BadCredentialsException Throws an error if the username does not exist
     */
    public boolean validate(User user) throws Exception {
        return userRepository.findByUsername(user.getUsername()).orElseThrow(Exception::new)
                .getPassword()
                .equals(user.getPassword());
    }
     
     /**
     * Update password by the provided username.
     * @param username the username that already exists on the repository.
     * @param oldPassword the password that the user currently uses.
     * @param newPassword the password that the user wants to switch to.
     * Get the current username from the user.
     * If the username is already in the database, then we can update the password
     */
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user != null) {
            if(user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
            else {
                // Username/Password invalid.
                return false;
            }
        }
        else {return false;}
    }
     
     /**
     * Updating the last name of the user
     * @param userID the user to be matched by userID
     * @param email the email to be changed to.
     * @return boolean if the userId exits then change the last name of the user
     * Get the userId from the employee.
     * If the last name is already in the database, then update the last name
     */
    public boolean updateEmail(int userID, String email) {
        User user = userRepository.findByUserID(userID).orElse(null);
        if(user != null) {
            user.setEmail(email);
            userRepository.save(user);
            return true;
        }
        else {return false;}
    }
}
