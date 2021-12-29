package com.revature.RevRelay.controllers;

import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserLoginAuthRequest;
import com.revature.RevRelay.models.dtos.UserAuthResponse;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.security.TokenAuthProvider;
import com.revature.RevRelay.services.UserService;
import com.revature.RevRelay.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


//For hitting this endpoint *No authentication
@RestController @CrossOrigin("*")
@RequestMapping(value = "/public/users")
public class PublicUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private TokenAuthProvider tokenAuthProvider;

    /**
     * Creates user in database from RequestBody, then passes RequestBody to login.
     * @param userAuthRequest UserAuthRequest object containing Username and Password.
     * @return Returns ResponseEntity from login containing JWT.
     * @throws Exception Throws BadCredentialsException on login failure.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@RequestBody UserRegisterAuthRequest userAuthRequest) throws IllegalArgumentException {
        try {
            if (userAuthRequest.getPassword() != null && userAuthRequest.getPassword().length() > 6) {
                userService.createUser(userAuthRequest);
                return login(new UserLoginAuthRequest(userAuthRequest.getUsername(), userAuthRequest.getPassword()));
            }
            else {
                return new ResponseEntity<>("Minimum Password Length 7 Characters", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns JWT when provided correct credentials.
     * @param userAuthRequest UserAuthRequest object containing Username and Password.
     * @return Returns ResponseEntity containing JWT.
     * @throws Exception Throws BadCredentialsException on login failure.
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserLoginAuthRequest userAuthRequest) {
        //Refactor tokenAuthProvider to call Login for users
        try {
            tokenAuthProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthRequest.getUsername(), userAuthRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("Incorrect username or password", new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        final User user = userService
                .loadUserByUsername(userAuthRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new UserAuthResponse(jwt));
    }

}
