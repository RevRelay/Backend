package com.revature.RevRelay.security;

import com.revature.RevRelay.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TokenAuthProvider extends AbstractUserDetailsAuthenticationProvider {
    @NonNull @Autowired
    UserService userService;

    /**
     * Used by the authenticate method of AbstractUserDetailsAuthenticationProvider for password authentication.
     * Note - this should probably be updated in the future to support encryption of backend passwords. - NL
     * @param d UserDetails as determined by Username being authenticated.
     * @param auth Authentication Token originally passed to the authenticate method.
     */
    @Override
    protected void additionalAuthenticationChecks(final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
        String password = (String) auth.getCredentials();
        if (!Objects.equals(d.getPassword(), password)) {
            throw new BadCredentialsException("Bad Credentials");
        }
    }

    @Override
    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {
        return userService.loadUserByUsername(username);
    }
}
