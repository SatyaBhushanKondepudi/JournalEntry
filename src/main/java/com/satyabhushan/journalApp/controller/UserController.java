package com.satyabhushan.journalApp.controller;

import com.satyabhushan.journalApp.entity.JournalEntry;
import com.satyabhushan.journalApp.entity.User;
import com.satyabhushan.journalApp.service.JournalEntryService;
import com.satyabhushan.journalApp.service.UserService;
import jakarta.websocket.server.PathParam;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService ;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userInDb = userService.findByUserName(username);
        if (userInDb.isPresent()) {
            userInDb.get().setUserName(user.getUserName());
            userInDb.get().setPassword(user.getPassword());
            userService.saveUserEntryByEncryptedPassword(userInDb.get());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userInDb = userService.findByUserName(username);
        userInDb.ifPresent(user -> userService.deleteUserByUserName(userInDb.get().getUserName()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }







}
