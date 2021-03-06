package com.revature.RevRelay.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for registering a new user
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserRegisterAuthRequest {
    private String username;
    private String email;
    private String displayName;
    private String password;
}
