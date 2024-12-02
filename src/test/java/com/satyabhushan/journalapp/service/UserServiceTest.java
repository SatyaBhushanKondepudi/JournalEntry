package com.satyabhushan.journalapp.service;

import com.satyabhushan.journalapp.entity.User;
import com.satyabhushan.journalapp.exceptions.UserAlreadyExists;
import com.satyabhushan.journalapp.exceptions.UserNotFoundWithGivenUserName;
import com.satyabhushan.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @CsvSource({"Satya" , "Sravani"})
    void testFindUserByUserName(String name) throws UserNotFoundWithGivenUserName {
        /*
        If userName is "Sravani" then throw UserNotFoundWithGivenUserName
        Else Mock the userRepository.findByUserName(name) and return a User object
        Call userService.findUserByUserName(name) and assert that the returned User object is not null
         */
        if(name.equals("Sravani")) {
            when(userRepository.findByUserName(name)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundWithGivenUserName.class, () -> userService.findByUserName(name));
        }
        else{
            when(userRepository.findByUserName(name)).thenReturn(Optional.ofNullable(User.builder().userName(name).password("password").build()));
            assertNotNull(userService.findByUserName(name));
        }
    }

    @ParameterizedTest
    @CsvSource({"Satya" , "Sravani"})
    void testCheckUserWithUserName(String name) {
        /*
        I need to mock the userRepository.findByUserName(name) and return a User object if name is "Sravani" Else return null
         */
        if(name.equals("Sravani")) {
            when(userRepository.findByUserName(name)).thenReturn(Optional.ofNullable(User.builder().userName(name).password("password").build()));
            assertThrows(UserAlreadyExists.class, () -> userService.checkUserWithUserName(name));
        }
        else{
            when(userRepository.findByUserName(name)).thenReturn(Optional.empty());
            assertDoesNotThrow(() -> userService.checkUserWithUserName(name));
        }
    }


    @Test
    void testGetAllUsers(){
        when(userRepository.findAll()).thenReturn(java.util.Arrays.asList(User.builder().userName("Satya").password("password").build()));
        assertEquals(1 , userService.getAll().size());
        assertNotNull(userService.getAll());
    }

    @Test
    void testFindById(){
        when(userRepository.findById(new ObjectId())).thenReturn(Optional.empty());
        assertFalse(userService.findById(new ObjectId()).isPresent());
    }

}
