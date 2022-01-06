package com.revature.RevRelay.controllers;

import com.google.gson.Gson;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserDTO;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.models.dtos.UserUpdateDTO;
import com.revature.RevRelay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            return new ResponseEntity<UserDTO>(new UserDTO(userService.loadUserByToken(tokenParsed)), responseHeaders, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.toString(), responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Takes in a UserUpdateDTO with email, firstName, lastName, birthDate,and displayName all in String format.
     * In the userSerivice the date is converted from a String into a date. All info from the UserUpdateDTO is used to
     * update the current user information.
     *
     * Returns a UserDTO with all this updated info in the correct format.
     *
     * @param token JWT of currently logged-in user from Authorization header.
     * @param changedInfoUser UserUpdateDTO with email, firstName, lastName, birthDate,and displayName all in String format.
     * @return ResponseEntity containing current user (UserDTO) with their updated info.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateAnyInfo(@RequestHeader("Authorization") String token, @RequestBody UserUpdateDTO changedInfoUser){
        String tokenParsed = token.replace("Bearer", "").trim();
        return ResponseEntity.ok(userService.updateUser(tokenParsed, changedInfoUser));
    }

    /**
     * Gets a user by their userID.
     *
     * @param userID user being retrieved
     * @return response entity 200 signaling successful creation
     */
    @GetMapping("/{userID}")
    public ResponseEntity<?> findByUserID(@PathVariable int userID) {
        return ResponseEntity.ok(userService.loadUserDTOByUserID(userID));
    }

    /**
     * Updates a user's password using the userID as the identifier, user needs to
     * input old password, new password, and confirm new password again
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
}
