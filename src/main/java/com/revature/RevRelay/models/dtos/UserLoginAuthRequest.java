package com.revature.RevRelay.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for logging in an existing user
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserLoginAuthRequest {
    private String username;
    private String password;
}
