package com.revature.RevRelay.services;

import com.revature.RevRelay.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

}