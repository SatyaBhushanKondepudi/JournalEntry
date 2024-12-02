package com.satyabhushan.journalapp.controller;

import com.satyabhushan.journalapp.entity.User;
import com.satyabhushan.journalapp.exceptions.UserNotFoundWithGivenUserName;
import com.satyabhushan.journalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService ;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) throws UserNotFoundWithGivenUserName {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUserName(username);
        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userService.saveUserEntryByEncryptedPassword(userInDb);
        return new ResponseEntity<>("User is successfully updated" , HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() throws UserNotFoundWithGivenUserName {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUserName(username);
        userService.deleteUserByUserName(userInDb.getUserName());
        return new ResponseEntity<>("User with userName " + username + "Deleted Successfully",HttpStatus.OK);
    }
}
