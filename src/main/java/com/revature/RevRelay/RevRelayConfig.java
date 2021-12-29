package com.revature.RevRelay;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Contains independent beans not better placed in another class.
 */
@Configuration
public class RevRelayConfig {

    /**
     * Provides a BCryptPasswordEncoder as an injectable bean.
     * 
     * @return A new BCryptPasswordEncoder with no other configuration or frills.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
