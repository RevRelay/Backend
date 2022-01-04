package com.revature.RevRelay.controllers;

import com.google.gson.Gson;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserDTO;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;

/**
 * Communication layer for endpoints, communicates with UserService to persist
 * to database
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    // used for converting json object and parse to string
    Gson g = new Gson();

    @Autowired
    private UserService userService;

    /**
     * Creates a new user and persists to database using userService layer
     *
     * @param user user being created
     * @return response entity 200 signaling successful creation
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegisterAuthRequest user) {

        return ResponseEntity.ok(userService.createUser(user));
    }




    /**
     * Returns a UserDTO representing the logged-in user via JWT.
     *
     * @param token JWT of currently logged-in user from Authorization header.
     * @return ResponseEntity containing current user.
     */
    @GetMapping("/current")
    ResponseEntity<?> getCurrent(@RequestHeader("Authorization") String token) {
        String tokenParsed = token.replace("Bearer", "").trim();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setBearerAuth(tokenParsed);
        try {
            return new ResponseEntity<UserDTO>(new UserDTO(userService.findByToken(tokenParsed)), responseHeaders, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.toString(), responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets a user by their userID.
     *
     * @param userID user being retrieved
     * @return response entity 200 signaling successful creation
     */
    @GetMapping("/{userID}")
    public ResponseEntity<?> findByUserID(@PathVariable int userID){
        return ResponseEntity.ok(userService.loadUserDTOByUserID(userID));
    }

    /**
     * Updates a user's first name using the userID as the identifier
     *
     * @param userID    userId of first name being changed
     * @param firstName user information being changed
     * @return response entity 200 signaling successful update
     */
    @PutMapping("/firstName/{userID}")
    public ResponseEntity<?> updateFirstName(@PathVariable int userID, @RequestBody String firstName) {
        User user = g.fromJson(firstName, User.class);
        String s = user.getFirstName();
        return ResponseEntity.ok(userService.updateFirstName(userID, s));
    }

    /**
     * Updates a user's last name using the userID as the identifier
     *
     * @param userID   userId of user being updated
     * @param lastName user information being changed
     * @return response entity 200 signaling successful update
     */
    @PutMapping("/lastName/{userID}")
    public ResponseEntity<?> updateLastName(@PathVariable int userID, @RequestBody String lastName) {
        User user = g.fromJson(lastName, User.class);
        String s = user.getLastName();
        return ResponseEntity.ok(userService.updateLastName(userID, s));
    }

    /**
     * Updates a user's password using the userID as the identifier, user needs to
     * input old password,
     * new password, and confirm new password again
     *
     * @param userID userId of user being updated
     * @param json   json object of password array, old password and two new
     *               passwords
     * @return response entity 200 signaling successful update
     */
    @PutMapping("/password/{userID}")
    public ResponseEntity<?> updatePassword(@PathVariable int userID, @RequestBody Map<String, String> json) {
        String oldPassword = json.get("oldPassword");
        String newPassword = json.get("newPassword");
        String confirmPassword = json.get("confirmPassword");
        return ResponseEntity.ok(userService.updatePassword(userID, oldPassword, newPassword, confirmPassword));
    }

    /**
     * Updates a user's display name using the userID as the identifier
     *
     * @param userID      userId of user being updated
     * @param displayName user information being changed
     * @return response entity 200 signaling successful update
     */
    @PutMapping("/displayName/{userID}")
    public ResponseEntity<?> updateDisplayName(@PathVariable int userID, @RequestBody String displayName) {
        User user = g.fromJson(displayName, User.class);
        String s = user.getDisplayName();
        return ResponseEntity.ok(userService.updateDisplayName(userID, s));
    }

    /**
     * Updates a user's birthday using the userID as the identifier
     *
     * @param userID    userId of user being updated
     * @param birthDate user information being changed
     * @return response entity 200 signaling successful update
     */
    @PutMapping("/birthDate/{userID}")
    public ResponseEntity<?> updateBirthDate(@PathVariable int userID, @RequestBody Date birthDate) {
        User user = g.fromJson(birthDate.toString(), User.class);
        Date s = user.getBirthDate();
        return ResponseEntity.ok(userService.updateBirthDate(userID, s));
    }
}
