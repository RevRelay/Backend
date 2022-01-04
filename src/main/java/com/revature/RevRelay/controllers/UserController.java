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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        return ResponseEntity.ok(userService.updateFirstName(userID, firstName.substring(1,firstName.length()-1)));
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
        return ResponseEntity.ok(userService.updateLastName(userID, lastName.substring(1,lastName.length()-1)));
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
        return ResponseEntity.ok(userService.updateDisplayName(userID, displayName.substring(1,displayName.length()-1)));
    }

    /**
     * Updates a user's birthday using the userID as the identifier
     *
     * @param userID    userId of user being updated
     * @param birthDate user information being changed
     * @return response entity 200 signaling successful update
     */
    @PutMapping("/birthDate/{userID}")
    public ResponseEntity<?> updateBirthDate(@PathVariable int userID, @RequestBody String birthDate) {
        Date s = null;
        try {
            s = new SimpleDateFormat("yyyy/MM/dd").parse(birthDate.substring(1,birthDate.length()-1));
        } catch(Exception e) {
            return null;
        }
        return ResponseEntity.ok(userService.updateBirthDate(userID, s));
    }

    /**
     * Updates a user's email using the userID as the identifier
     *
     * @param userID      userId of user being updated
     * @param email user information being changed
     * @return response entity 200 signaling successful update
     */
    @PutMapping("/email/{userID}")
    public ResponseEntity<?> updateEmail(@PathVariable int userID, @RequestBody String email) {
        return ResponseEntity.ok(userService.updateEmail(userID, email.substring(1,email.length()-1)));
    }

}
