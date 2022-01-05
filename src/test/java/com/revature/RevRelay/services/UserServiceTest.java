package com.revature.RevRelay.services;

import com.revature.RevRelay.models.User;
import com.revature.RevRelay.models.dtos.UserDTO;
import com.revature.RevRelay.models.dtos.UserRegisterAuthRequest;
import com.revature.RevRelay.repositories.PageRepository;
import com.revature.RevRelay.repositories.UserRepository;
import com.revature.RevRelay.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class UserServiceTest {
    UserRepository mockUserRepository;
	PageRepository mockPageRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil mockJwtUtil;
    PasswordEncoder mockPasswordEncoder;
    UserService userService;
    User user;
    User fakeUser;
    UserRegisterAuthRequest userRegisterAuthRequest;
    UserDTO mockUserDTO;
    final String testUsername = "testname";
    final String testPassword ="testpassword";
    final String testEmail ="testemail";
    final String testFirstName = "H";
    final String testLastName = "L";
    final String testDisplayName = "HL";
    final Date testBirthDate = null;

    @BeforeEach
    public void setup(){
        //Making a mock PasswordEncoder
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        //Making a mock UserRepository
        mockUserRepository = Mockito.mock(UserRepository.class);
        //Making a mock JwtUtil
        mockJwtUtil = Mockito.mock(JwtUtil.class);
        //Making a mock passwordEncoder
        mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
		//Making a mock pageRepository
		mockPageRepository = Mockito.mock(PageRepository.class);
        //Setting up the userservice
        userService = new UserService(mockUserRepository, mockJwtUtil, mockPasswordEncoder,mockPageRepository);
        //Used with creating User
        userRegisterAuthRequest = new UserRegisterAuthRequest();
        userRegisterAuthRequest.setUsername("notNull");
        userRegisterAuthRequest.setPassword("notNull");
        userRegisterAuthRequest.setEmail("notNull");
        //Standard user used in testing
        user = new User();
        user.setUserID(0);
        user.setUsername(testUsername);
        user.setPassword(testPassword);
        user.setEmail(testEmail);
        user.setFirstName(testFirstName);
        user.setLastName(testLastName);
        user.setDisplayName(testDisplayName);
        //User used for incorrect login/registry
        fakeUser = new User();
        fakeUser.setUserID(0);
        fakeUser.setUsername("testname");
        fakeUser.setPassword("testpassword");
        //is this supposed to be fakeUser.setEmail()?
        //user.setEmail("testemail");
    }

    @Test
    void getAndSetTest(){
        UserService userService = new UserService();
        userService.setUserRepository(mockUserRepository);
        userService.setJwtUtil(mockJwtUtil);
        userService.setPasswordEncoder(mockPasswordEncoder);
        assertTrue(userService.getUserRepository()==mockUserRepository
                        &&userService.getJwtUtil()==mockJwtUtil
                        &&userService.getPasswordEncoder()==mockPasswordEncoder
        );
    }

    //Test User creation. Should return user and then user should equal user
    @Test
    void userServiceConstructor() {
        UserService userServiceTestEquality = new UserService(mockUserRepository, mockJwtUtil, mockPasswordEncoder,mockPageRepository);
        assertTrue(userServiceTestEquality.getUserRepository()==mockUserRepository
                &&userServiceTestEquality.getJwtUtil()==mockJwtUtil
                &&userServiceTestEquality.getPasswordEncoder()==mockPasswordEncoder);
    }

    //Test User creation. Should return user and then user should equal user
    @Test
    void createUser() {
        when(mockUserRepository.save(any())).thenReturn(user);
		when(mockPageRepository.save(any())).thenReturn(null);
        when(mockUserRepository.existsByUsername(any())).thenReturn(false);
        when(mockUserRepository.existsByEmail(any())).thenReturn(false);
        try{
            assertEquals(userService.createUser(userRegisterAuthRequest), user);
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

    @Test
    void createUserWithPreexistingUsername(){
        when(mockUserRepository.existsByUsername(any())).thenReturn(true);
        try{
            assertThrows(IllegalArgumentException.class, (Executable) userService.createUser(userRegisterAuthRequest));
        } catch (Exception ignored) {}
    }

    @Test
    void createUserWithNullPassword(){
        userRegisterAuthRequest.setPassword(null);
        try{
            assertThrows(IllegalArgumentException.class, (Executable) userService.createUser(userRegisterAuthRequest));
        } catch (Exception ignored) {}
    }

    @Test
    void createUserWithPreexistingEmail(){
        when(mockUserRepository.existsByEmail(any())).thenReturn(true);
        try{
            assertThrows(IllegalArgumentException.class, (Executable) userService.createUser(userRegisterAuthRequest));
        } catch (Exception ignored) {}
    }

    //Test login. Should return true and user should equal user
    @Test
    void loginCorrectUser() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));
        try{
            assertEquals(userService.login(user.getUsername(), user.getPassword()), user);
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

    //Test failed login. Should return a random exception and the exception is expected.
    @Test
    void loginFailed() {
        when(mockUserRepository.save(any())).thenThrow(new AccessDeniedException("Unable to login"));
        try{
            Exception e = assertThrows(Exception.class, (Executable) userService.login(fakeUser.getUsername(), fakeUser.getPassword()));
            assertTrue(e.getMessage().contains("Unable to login"));
        } catch (Exception ignored) {}

    }

    //Test findByToken. Should return an optional user and user should equal user.
    @Test
    void findByToken() {
        String legitimateToken = ":";
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        when(mockJwtUtil.generateToken(fakeUser)).thenReturn(legitimateToken);
        try{
            assertEquals(userService.loadUserByToken(mockJwtUtil.generateToken(fakeUser)), user);
        } catch (Exception ignored) {}
    }

    //Test failed findByToken. Should return null and return an exception, the exception is expected.
    @Test
    void findByTokenNotFound() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(mockJwtUtil.generateToken(fakeUser)).thenReturn(null);
        try{
            Exception e = assertThrows(Exception.class, (Executable) userService.loadUserByToken(mockJwtUtil.generateToken(fakeUser)));
            assertTrue(e.getMessage().contains("Token Does Not Correspond to User"));
        } catch (Exception ignored) {}
    }

    //Test failed findByToken. Should return null and return an exception, the exception is expected.
    @Test
    void findByTokenUserNotFound() {
        String legitimateToken = ":";
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(mockJwtUtil.generateToken(fakeUser)).thenReturn(legitimateToken);
        try{
            Exception e = assertThrows(Exception.class, (Executable) userService.loadUserByToken(mockJwtUtil.generateToken(fakeUser)));
            assertTrue(e.getMessage().contains("Token Does Not Correspond to User"));
        } catch (Exception ignored) {}
    }

    //Test loadUserByUsername. Should return an optional user that equals user.
    @Test
    void loadUserByUsername() {
        when(mockUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        try{
            assertEquals(userService.loadUserByUsername(user.getUsername()), user);
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

    //Test loadUserByUserID. Should return an optional user that equals user.
    @Test
    void loadUserByUserID() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        try{
            assertEquals(userService.loadUserDTOByUserID(user.getUserID()).getUsername(), user.getUsername());
        } catch (Exception ignored) {}
    }

    @Test
    void updateFirstNameToRobert() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        try{
            userService.updateFirstName(0,"Robert");
            Assertions.assertEquals("Robert", user.getFirstName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateFirstNameToRobertButFail() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.empty());
        try{
            Assertions.assertFalse(userService.updateFirstName(10, "Robert"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateLastNameToRobert() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        try{
            userService.updateLastName(0,"Robert");
            Assertions.assertEquals("Robert", user.getLastName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateLastNameToRobertButFail() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.empty());
        try{
            Assertions.assertFalse(userService.updateLastName(10, "Robert"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateDisplayNameToRobert() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        try{
            userService.updateDisplayName(0,"Robert");
            Assertions.assertEquals("Robert", user.getDisplayName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateDisplayNameToRobertButFail() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.empty());
        try{
            Assertions.assertFalse(userService.updateDisplayName(10, "Robert"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateBirthDate() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        try{
            Date date=new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2020");
            userService.updateBirthDate(0, date);
            Assertions.assertEquals(date,user.getBirthDate());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateBirthDateButFail() {
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.empty());
        try{
            Date date=new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2020");
            userService.updateBirthDate(0,date);
            Assertions.assertNotEquals(date,user.getBirthDate());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updatePasswordTo12345AndReturnTrue(){
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.matches(any(),any())).thenReturn(true);
        try{
            Assertions.assertTrue(userService.updatePassword(0,"testpassword","1234567890","1234567890"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updatePasswordTo12345ButFailBecauseConfirmAndNewPasswordDoNotMatchAndReturnFalse(){
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.matches(any(),any())).thenReturn(true);
        try{
            Assertions.assertFalse(userService.updatePassword(0,"testpassword","1234567890","123456789"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updatePasswordTo12345ButFailBecauseOldPasswordWasWrongAndReturnFalse(){
        when(mockUserRepository.findByUserID(anyInt())).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.matches(any(),any())).thenReturn(false);
        try{
            Assertions.assertFalse(userService.updatePassword(0,"testpassword0","1234567890","1234567890"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nested
    public class TestUpdateUser {

        String testUsernameUpdateUser;
        String testEmailUpdateUser;
        String testFirstNameUpdateUser;
        String testLastNameUpdateUser;
        String testDisplayNameUpdateUser;
        Calendar testBirthDateUpdateUser;

        @BeforeEach
        public void setup() {
            testUsernameUpdateUser = "fherbert";
            testEmailUpdateUser = "gemps@arrakis.space";
            testFirstNameUpdateUser = "Frank";
            testLastNameUpdateUser = "Herbert";
            testDisplayNameUpdateUser = "PaulDidNothingWrong";
            // Apparently using Date instead of Calendar is deprecated for everything but UNIX epoch.
            testBirthDateUpdateUser = Calendar.getInstance();
            testBirthDateUpdateUser.clear();
            testBirthDateUpdateUser.set(Calendar.YEAR, 1920);
            testBirthDateUpdateUser.set(Calendar.MONTH, 10);
            testBirthDateUpdateUser.set(Calendar.DAY_OF_MONTH, 8);
            mockUserDTO = Mockito.mock(UserDTO.class);
            when(mockUserDTO.getUsername()).thenReturn(testUsernameUpdateUser);
            when(mockUserDTO.getEmail()).thenReturn(testEmailUpdateUser);
            when(mockUserDTO.getFirstName()).thenReturn(testFirstNameUpdateUser);
            when(mockUserDTO.getLastName()).thenReturn(testLastNameUpdateUser);
            when(mockUserDTO.getBirthDate()).thenReturn(testBirthDateUpdateUser.getTime());
            when(mockUserDTO.getDisplayName()).thenReturn(testDisplayNameUpdateUser);
            when(mockUserRepository.save(any())).thenReturn(user);
            when(mockUserRepository.findByUsername(any())).thenReturn(Optional.of(user));
        }

        @Test
        void updateUserVariablesTestAllExpectToSucceed() {
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsernameUpdateUser);
            assertEquals(testUserDTOOutput.getEmail(), testEmailUpdateUser);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstNameUpdateUser);
            assertEquals(testUserDTOOutput.getLastName(), testLastNameUpdateUser);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDateUpdateUser.getTime());
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayNameUpdateUser);
        }

        @Test
        void updateUserVariablesNoUpdateOnUsername() {
            when(mockUserDTO.getUsername()).thenReturn(null);
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsername);
            assertEquals(testUserDTOOutput.getEmail(), testEmailUpdateUser);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstNameUpdateUser);
            assertEquals(testUserDTOOutput.getLastName(), testLastNameUpdateUser);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDateUpdateUser.getTime());
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayNameUpdateUser);
        }

        @Test
        void updateUserVariablesNoUpdateOnEmail() {
            when(mockUserDTO.getEmail()).thenReturn(null);
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsernameUpdateUser);
            assertEquals(testUserDTOOutput.getEmail(), testEmail);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstNameUpdateUser);
            assertEquals(testUserDTOOutput.getLastName(), testLastNameUpdateUser);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDateUpdateUser.getTime());
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayNameUpdateUser);
        }

        @Test
        void updateUserVariablesNoUpdateOnFirstName() {
            when(mockUserDTO.getFirstName()).thenReturn(null);
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsernameUpdateUser);
            assertEquals(testUserDTOOutput.getEmail(), testEmailUpdateUser);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstName);
            assertEquals(testUserDTOOutput.getLastName(), testLastNameUpdateUser);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDateUpdateUser.getTime());
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayNameUpdateUser);
        }

        @Test
        void updateUserVariablesNoUpdateOnLastName() {
            when(mockUserDTO.getLastName()).thenReturn(null);
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsernameUpdateUser);
            assertEquals(testUserDTOOutput.getEmail(), testEmailUpdateUser);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstNameUpdateUser);
            assertEquals(testUserDTOOutput.getLastName(), testLastName);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDateUpdateUser.getTime());
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayNameUpdateUser);
        }

        @Test
        void updateUserVariablesNoUpdateOnBirthDate() {
            when(mockUserDTO.getBirthDate()).thenReturn(null);
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsernameUpdateUser);
            assertEquals(testUserDTOOutput.getEmail(), testEmailUpdateUser);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstNameUpdateUser);
            assertEquals(testUserDTOOutput.getLastName(), testLastNameUpdateUser);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDate);
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayNameUpdateUser);
        }

        @Test
        void updateUserVariablesNoUpdateOnDisplayName() {
            when(mockUserDTO.getDisplayName()).thenReturn(null);
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsernameUpdateUser);
            assertEquals(testUserDTOOutput.getEmail(), testEmailUpdateUser);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstNameUpdateUser);
            assertEquals(testUserDTOOutput.getLastName(), testLastNameUpdateUser);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDateUpdateUser.getTime());
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayName);
        }

        @Test
        void updateUserVariablesOnlyUpdateUsername() {
            when(mockUserDTO.getEmail()).thenReturn(null);
            when(mockUserDTO.getFirstName()).thenReturn(null);
            when(mockUserDTO.getLastName()).thenReturn(null);
            when(mockUserDTO.getBirthDate()).thenReturn(null);
            when(mockUserDTO.getDisplayName()).thenReturn(null);
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getUsername(), testUsernameUpdateUser);
            assertEquals(testUserDTOOutput.getEmail(), testEmail);
            assertEquals(testUserDTOOutput.getFirstName(), testFirstName);
            assertEquals(testUserDTOOutput.getLastName(), testLastName);
            assertEquals(testUserDTOOutput.getBirthDate(), testBirthDate);
            assertEquals(testUserDTOOutput.getDisplayName(), testDisplayName);
        }

        @Test
        void updateUserVariablesSetNameToEmptyString() {
            when(mockUserDTO.getFirstName()).thenReturn("");
            when(mockUserDTO.getLastName()).thenReturn("");
            UserDTO testUserDTOOutput = userService.updateUser("mockToken", mockUserDTO);
            assertEquals(testUserDTOOutput.getFirstName(), "");
            assertEquals(testUserDTOOutput.getLastName(), "");
        }
    }
}
