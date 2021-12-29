package com.revature.RevRelay.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for auth response to return a jwt for Spring Security verification
 */
@Getter @Setter @AllArgsConstructor
public class UserAuthResponse {private final String jwt;}
