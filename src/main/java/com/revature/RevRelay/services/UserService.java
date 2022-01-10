package com.revature.RevRelay.services;

import com.revature.RevRelay.models.Chatroom;
import com.revature.RevRelay.models.Group;
import com.revature.RevRelay.models.Page;
import com.revature.RevRelay.models.dtos.UserDTO;
import com.revature.RevRelay.models.dtos.UserUpdateDTO;
import com.revature.RevRelay.repositories.GroupRepository;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.UserRepository;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.utils.JwtUtil;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/* TODO - Add tests for email service */

/**
 * Returns a UserService object, which allows a User to update information on
 * their personal page
 */
@Service
@NoArgsConstructor
@Getter
@Setter
public class UserService implements UserDetailsService {

    private PageService pageService;
	private GroupService groupService;
	private ChatroomService chatroomService;


    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    /**
     * All args constructor
     *
     * @param userRepository  UserRepository object autowired
     * @param jwtUtil         JwtUtil object autowired
     * @param passwordEncoder PasswordEncoder object autowired
     */
    @Autowired
    UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, PageService pageService, GroupService groupRepository,ChatroomService chatroomService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.pageService = pageService;
		this.groupService = groupRepository;
		this.chatroomService = chatroomService;
    }

    /**
     * Logs in the user with the given username and password, then returns that
     * User.
     *
     * @param username the username to match.
     * @param password the password to match.
     * @return User of the given username AND password.
     * @throws AccessDeniedException Thrown when either a user cannot be loaded
     *                               or when there's a password mismatch.
     * @deprecated This method is marked for deletion as its function is handled
     * by the authentication method of the TokenAuthProvider class. - NL
     */
    public User login(String username, String password) throws AccessDeniedException {
        try {
            User user = loadUserByUsername(username);
            if (user.getPassword().equals(password))
                return user;
        } catch (Exception e) {
        } finally {
            throw new AccessDeniedException("Incorrect username/password");
        }
    }

    /**
     * Takes a user persists it then returns the user
     *
     * @param userAuthRequest The Auth Request corresponding to the user that is
     *                        going to be created
     * @return the full user object that was persisted is returned.
     */
    public User createUser(UserRegisterAuthRequest userAuthRequest) throws IllegalArgumentException {
        if (!isValidUsername(userAuthRequest.getUsername())) {
            throw new IllegalArgumentException("Username Not Valid");
        } else if (!isValidPassword(userAuthRequest.getPassword())) {
            throw new IllegalArgumentException("Password Not Valid");
        } else if (!isValidEmail(userAuthRequest.getEmail())) {
            throw new IllegalArgumentException("Email Not Valid");
        } else {
            User user = new User();
            user.setDisplayName(userAuthRequest.getDisplayName());
            user.setEmail(userAuthRequest.getEmail());
            user.setUsername(userAuthRequest.getUsername());
            user.setPassword(passwordEncoder.encode(userAuthRequest.getPassword()));
            user = userRepository.save(user);
            Page p = new Page(user);
            pageService.createPage(p);
            user.setUserPage(p);
            return userRepository.save(user);
        }
    }

    /**
     * Retrieves user by UserID without password
     *
     * @param userID userID expected to be in database.
     * @return UserDTO created from a User object in database.
     */
    public UserDTO loadUserDTOByUserID(int userID) {
        Optional<User> user = userRepository.findByUserID(userID);
        assert user.isPresent();
        return new UserDTO(user.get());
    }

    /**
     * Implementation of UserDetailsService method for Spring Security.
     *
     * @param username Username expected to be in database.
     * @return User object from database.
     * @throws UsernameNotFoundException Throws exception on empty optional from
     *                                   repository.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Username Not Found");
        }
    }

    /**
     * Searches for a user by extracting the username from the jwtUtil
     *
     * @param token Token with information about the username inside the token
     * @return returns optional user
     * @throws UsernameNotFoundException Throws exception if token does not exist
     *                                   OR optional is null
     */
    public User loadUserByToken(String token) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(jwtUtil.extractUsername(token));
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Token Does Not Correspond to User");
        }
    }

    /**
     * Single function for updating a User (identified by a JWT) via a UserDTO.
     * Updates Username, Email, FirstName, LastName, BirthDate, and DisplayName.
     * Not suitable for updating a password.
     * Note that the isValid functions do allow empty strings (for deleting
     * user information from the database) while not allowing nulls. The exception is
     * BirthDate, which could be solved in a few ways (very distant past date == delete?).
     * Invalid inputs are ignored without feedback to the user; possible point
     * of improvement. - NL
     *
     * @param token   JWT corresponding to a User in the database.
     * @param userDTO UserDTO deserialized from a Controller query.
     * @return UserDTO object corresponding to the User after edits.
     * @throws UsernameNotFoundException If Token fails to find a User.
     */
    public UserDTO updateUser(String token, UserDTO userDTO) throws UsernameNotFoundException {
        User user = loadUserByToken(token);
        if (isValidUsername(userDTO.getUsername())) {
            user.setUsername(userDTO.getUsername());
        }
        if (isValidEmail(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
        }
        if (isValidFirstName(userDTO.getFirstName())) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (isValidLastName(userDTO.getLastName())) {
            user.setLastName(userDTO.getLastName());
        }
        if (isValidBirthDate(userDTO.getBirthDate())) {
            user.setBirthDate(userDTO.getBirthDate());
        }
        if (isValidDisplayName(userDTO.getDisplayName())) {
            user.setDisplayName(userDTO.getDisplayName());
        }
        return new UserDTO(userRepository.save(user));
    }

    /**
     * Borrows the method from NL, overloaded to take updateUserDTO - AL
     * See above comment, Doesn't allow for null entries
     * Method overloads for userUpdate to update a user from the frontend
     *
     * @param token   JWT corresponding to a User in the database.
     * @param userDTO UserDTO deserialized from a Controller query.
     * @return UserDTO object corresponding to the User after edits.
     * @throws UsernameNotFoundException If Token fails to find a User
     */
    public UserDTO updateUser(String token, UserUpdateDTO userDTO) throws UsernameNotFoundException {
        User user = loadUserByToken(token);
        if (isUpdateValidEmail(userDTO.getEmail(), user)) {
            user.setEmail(userDTO.getEmail());
        }
        if (isValidFirstName(userDTO.getFirstName())) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (isValidLastName(userDTO.getLastName())) {
            user.setLastName(userDTO.getLastName());
        }
        if (isValidBirthDate(userDTO.getBirthDate())) {
            Date s = null;
            try {
                s = new SimpleDateFormat("yyyy-MM-dd").parse(userDTO.getBirthDate().substring(0, 10));
            } catch (Exception e) {
                return null;
            }
            user.setBirthDate(s);
        }
        if (isValidDisplayName(userDTO.getDisplayName())) {
            user.setDisplayName(userDTO.getDisplayName());
        }
        return new UserDTO(userRepository.save(user));
    }

    /**
     * Attempts to update a User's password
     * Takes in the old password, the desired new password, and another instance of
     * the new password for confirmation
     * If the old password is incorrect, or the new password does not match the
     * confirmation, returns false
     * If a User's password is successfully updated, save the User and return true
     *
     * @param userID          The User's unique ID
     * @param oldPassword     The User's old password
     * @param newPassword     The User's desired new password
     * @param confirmPassword The User's desired new password
     * @return True if the update succeeds, or else false
     */
    public boolean updatePassword(int userID, String oldPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByUserID(userID).orElse(null);
        if (user != null) {
            if (passwordEncoder.matches(oldPassword, user.getPassword())
                    && (newPassword.equals(confirmPassword))
                    && (isValidPassword(newPassword))) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies that a username is suitable based on our constraints.
     *
     * @param username New username.
     * @return True if valid, false if invalid.
     */
    private boolean isValidUsername(String username) {
        return (!userRepository.existsByUsername(username) && username != null);
    }

    /**
     * Verifies that a password is suitable based on our constraints.
     *
     * @param password New password prior to hashing and storage to database.
     * @return True if valid, false if invalid.
     */
    private boolean isValidPassword(String password) {
        return (password != null);
    }

    /**
     * Verifies that a email is suitable based on our constraints.
     *
     * @param email New email.
     * @return True if valid, false if invalid.
     */
    private boolean isValidEmail(String email) {
        return (!userRepository.existsByEmail(email) && email != null);
    }

    private boolean isUpdateValidEmail(String email, User user){
        return (email == user.getEmail() || isValidEmail(email));
    }

    /**
     * This method allows for allowing friends to be added to a user.
     * @param userID of user who wishes to add a friend
     * @param friendUsername the username of the friend.
     * @return the friend that was added
     * @throws Exception
     */
    public User addFriend(int userID, String friendUsername) throws Exception {
        User friend = userRepository.findByUsername(friendUsername).orElseThrow(() -> new Exception("No friend Found"));
        User user = userRepository.findByUserID(userID).orElseThrow(() -> new Exception("No person Found"));
        if (user.getUsername().equals(friend.getUsername())) {
            return friend;
        }
        Set<User> yourFriendsSet = user.getFriends();
		Set<User> otherFriendsSet = friend.getFriends();
		if (!otherFriendsSet.contains(user))
			otherFriendsSet.add(user);
		if (!yourFriendsSet.contains(friend))
			yourFriendsSet.add(friend);
        user.setFriends(yourFriendsSet);
		friend.setFriends(otherFriendsSet);
        userRepository.save(user);
        return friend;
    }
    /**
     * Verifies that a firstName is suitable based on our constraints.
     *
     * @param firstName New firstName.
     * @return True if valid, false if invalid.
     */
    private boolean isValidFirstName(String firstName) {
        return (firstName != null);
    }

    /**
     * Verifies that a lastName is suitable based on our constraints.
     *
     * @param lastName New lastName.
     * @return True if valid, false if invalid.
     */
    private boolean isValidLastName(String lastName) {
        return (lastName != null);
    }

    /**
     * Verifies that a displayName is suitable based on our constraints.
     *
     * @param displayName New displayName.
     * @return True if valid, false if invalid.
     */
    private boolean isValidDisplayName(String displayName) {
        return (displayName != null);
    }

    /**
     * Verifies that a birthDate is suitable based on our constraints.
     *
     * @param birthDate New displayName.
     * @return True if valid, false if invalid.
     */
    private boolean isValidBirthDate(Date birthDate) {
        Date minBirthDate = new Date(-2208988800000L); // Jan 1st 1900, 12:00 AM
        return (birthDate != null && birthDate.after(minBirthDate));
    }

    /**
     * Verifies that a birthDate is suitable based on our constraints when it is a String.
     *
     * @param birthDate New displayName.
     * @return True if valid, false if invalid.
     */
    private boolean isValidBirthDate(String birthDate) {
        return (birthDate != null);
    }

	/**
	 * IM SICK AND TIRED OF CONSTRAIN ERRORS THIS WILL FIX THEM
	 * @param user user to delete
	 * @return if delete was successfully completed
	 */
	@Transactional
	public boolean delete(User user){
		User userToDelete = userRepository.findById(user.getUserID()).orElse(null);
		if (userToDelete ==null)
			return false;
		Page userPageToDelete = userToDelete.getUserPage();

		pageService.delete(userPageToDelete);
		userToDelete.setUserPage(null);
		Set<Group> groupsToDelete = userToDelete.getOwnedGroups();
		userToDelete.setOwnedGroups(null);
		userToDelete = userRepository.save(userToDelete);
		groupsToDelete.forEach(group -> groupService.delete(group));
		Set<Group> groupsToRemoveUser = userToDelete.getUserGroups();
		userToDelete.setUserGroups(null);
		userToDelete = userRepository.save(userToDelete);
		User finalUserToDelete = userToDelete;
		groupsToRemoveUser.forEach(group -> groupService.deleteMember(group.getGroupID(), finalUserToDelete.getUserID()));
		Set<Chatroom> removeUserFromChatroom = userToDelete.getChatRooms();
		userToDelete.setChatRooms(null);
		userToDelete = userRepository.save(userToDelete);
		User finalUserToDelete1 = userToDelete;
		removeUserFromChatroom.forEach(chatroom -> chatroomService.removeMember(chatroom.getChatID(), finalUserToDelete1.getUserID()));

		Set<User> removeFriendSet = userToDelete.getFriends();
		System.out.println(removeFriendSet.size());
		userToDelete.setFriends(null);
		userToDelete = userRepository.save(userToDelete);

		User finalUserToDelete2 = userToDelete;
		removeFriendSet.forEach(friend-> {
			Set<User> u = friend.getFriends();
			u.removeIf(user1 -> user1.getUserID()== finalUserToDelete2.getUserID());
			friend.setFriends(u);
			userRepository.save(friend);
		});



		userRepository.delete(userToDelete);
		return true;
	}
	/**
	 * Deletes all users
	 * @return count of deleted users
	 */
	public int deleteAll(){
		AtomicInteger count = new AtomicInteger(0);
		List<com.revature.RevRelay.models.User> users = userRepository.findAll();
		users.forEach(user -> {if(delete(user)) count.getAndIncrement();});
		System.out.println("Deleted "+count+" User");
		return count.get();
	}
}
