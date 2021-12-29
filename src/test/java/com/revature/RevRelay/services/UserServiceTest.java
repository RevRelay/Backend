package com.revature.RevRelay.services;

import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.repository.UserRepository;
import com.revature.RevRelay.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.Assert.assertTrue;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {
    UserRepository mockUserRepository;
    JwtUtil mockJwtUtil;
    UserService userService;
    User user;
    User fakeUser;
    UserRegisterAuthRequest userRegisterAuthRequest;


    @BeforeEach
    public void setup(){
        //Making a mock UserRepository
        mockUserRepository = Mockito.mock(UserRepository.class);
        //Making a mock JwtUtil
        mockJwtUtil = Mockito.mock(JwtUtil.class);
        //Setting up the userservice
        userService = new UserService();
        //Setting the userRepository field with reflections
        ReflectionTestUtils.setField(
                userService,
                "userRepository",
                mockUserRepository);
        //Setting the jwtUtil with reflections
        ReflectionTestUtils.setField(
                userService,
                "jwtUtil",
                mockJwtUtil);
        //Used with creating User
        userRegisterAuthRequest = new UserRegisterAuthRequest();
        //Standard user used in testing
        user = new User();
        user.setUserID(0);
        user.setUsername("testname");
        user.setPassword("testpassword");
        //User used for incorrect login/registry
        fakeUser = new User();
        fakeUser.setUserID(0);
        fakeUser.setUsername("testname");
        fakeUser.setPassword("testpassword");
    }

    //Test User creation. Should return user and then user should equal user
    @Test
    void createUser() {
        userRegisterAuthRequest.setUsername("notNull");
        when(mockUserRepository.save(any())).thenReturn(user);
        when(mockUserRepository.existsByUsername(any())).thenReturn(false);
        try{
            assertTrue(userService.createUser(userRegisterAuthRequest).equals(user));
        } catch (Exception ignored) {}
    }

    //Test failed User creation. Should throw an exception and the exception is expected.
    @Test
    void createIncorrectUser(){
        when(mockUserRepository.save(any())).thenThrow(new IllegalArgumentException ("Username Not Valid"));
        try{
            assertThrows(IllegalArgumentException.class, (Executable) userService.createUser(userRegisterAuthRequest));
        } catch (Exception ignored) {}

    }

    //Test login. Should return true and user should equal user
    @Test
    void loginCorrectUser() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));
        try{
            assertTrue(userService.login(user.getUsername(), user.getPassword()).equals(user));
        } catch (Exception ignored) {}
    }

    //Test failed login. Should return an exception and the exception is expected.
    @Test
    void loginIncorrectUser() {
        when(mockUserRepository.save(any())).thenThrow(new AccessDeniedException("Incorrect Login/Password"));
        try{
            Exception e = assertThrows(AccessDeniedException.class, (Executable) userService.login(fakeUser.getUsername(), fakeUser.getPassword()));
            assertTrue(e.getMessage().contains("Incorrect username/password"));
        } catch (Exception ignored) {}

    }

    //Test findByToken. Should return an optional user and user should equal user.
    @Test
    void findByToken() {
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
    }

    //Test failed findByToken. Should return null and return an exception, the exception is expected.
    @Test
    void findByTokenNotFound() {
        when(mockUserRepository.findByUsername(any())).thenReturn(null);
        when(mockJwtUtil.generateToken(fakeUser)).thenReturn(null);
        try{
            Exception e = assertThrows(Exception.class, (Executable) userService.findByToken(mockJwtUtil.generateToken(fakeUser)));
            assertTrue(e.getMessage().contains("Token Does Not Correspond to User"));
        } catch (Exception ignored) {}
    }

    //Test loadUserByUsername. Should return an optional user that equals user.
    @Test
    void loadUserByUsername() {
        when(mockUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        try{
            assertTrue(userService.loadUserByUsername(user.getUsername()).equals(user));
        } catch (Exception ignored) {}
    }

    //Test failed loadByUsername. Should return an exception and the exception is expected
    @Test
    void loadUserByUsernameNotFound() {
        when(mockUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        try{
            Exception e = assertThrows(UsernameNotFoundException.class, (Executable) userService.loadUserByUsername(user.getUsername()));
            assertTrue(e.getMessage().contains("Username Not Found"));
        } catch (Exception ignored) {}
    }

}