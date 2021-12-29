package com.revature.RevRelay.controllers;
import com.google.gson.Gson;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.repositories.UserRepository;
import com.revature.RevRelay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    Gson g = new Gson();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.createNewUser(user));
    }

    @PutMapping("/firstName/{userID}")
    public ResponseEntity<?> updateFirstName(@PathVariable int userID, @RequestBody String firstName){
        User user = g.fromJson(firstName,User.class);
        String s = user.getFirstName();
        return ResponseEntity.ok(userService.updateFirstName(userID,s));
    }

    @PutMapping("/lastName/{userID}")
    public ResponseEntity<?> updateLastName(@PathVariable int userID, @RequestBody String lastName){
        User user = g.fromJson(lastName,User.class);
        String s = user.getLastName();
        return ResponseEntity.ok(userService.updateLastName(userID,s));
    }

    @PutMapping("/password/{userID}")
    public ResponseEntity<?> updatePassword(@PathVariable int userID, @RequestBody Map<String, String> json){
        String oldPassword = json.get("oldPassword");
        String newPassword = json.get("newPassword");
        String confirmPassword = json.get("confirmPassword");
        return ResponseEntity.ok(userService.updatePassword(userID,oldPassword,newPassword,confirmPassword));
    }

    @PutMapping("/displayName/{userID}")
    public ResponseEntity<?> updateDisplayName(@PathVariable int userID, @RequestBody String displayName){
        User user = g.fromJson(displayName,User.class);
        String s = user.getDisplayName();
        return ResponseEntity.ok(userService.updateDisplayName(userID,s));
    }

    @PutMapping("/birthDate/{userID}")
    public ResponseEntity<?> updateBirthDate(@PathVariable int userID, @RequestBody Date birthDate){
        User user = g.fromJson(birthDate.toString(),User.class);
        Date s = user.getBirthDate();
        return ResponseEntity.ok(userService.updateBirthDate(userID,s));
    }



}
