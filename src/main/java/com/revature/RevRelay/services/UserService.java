package com.revature.RevRelay.services;

import com.revature.RevRelay.persistence.UserRepository;
import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserLoginAuthRequest;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
//return to this later - NL
import org.omg.CosNaming.NamingContextPackage.NotFound;
  
@Service @NoArgsConstructor @Getter @Setter @AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *Logs in the user with the given username and password, then returns that User.
     * @param username the username to match.
     * @param password the password to match.
     * @return User of the given username AND password.
     * @throws AccessDeniedException
     */
    public User login(String username, String password) throws AccessDeniedException {
        try {
            User user = loadUserByUsername(username);
            if (user.getPassword().equals(password)) return user;
        }
        catch (Exception e) {}
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
            user.setPassword(passwordEncoder.encode(userAuthRequest.getPassword()));
            return userRepository.save(user);
        }
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
}
