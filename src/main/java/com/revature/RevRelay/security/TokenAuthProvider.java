package com.revature.RevRelay.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthProvider extends DaoAuthenticationProvider {

    @Autowired
    public TokenAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.setPasswordEncoder(passwordEncoder);
        this.setUserDetailsService(userDetailsService);
    }
}
