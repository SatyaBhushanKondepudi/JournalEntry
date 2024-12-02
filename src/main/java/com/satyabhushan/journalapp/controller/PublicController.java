package com.satyabhushan.journalapp.controller;

import com.satyabhushan.journalapp.entity.User;
import com.satyabhushan.journalapp.exceptions.UserAlreadyExists;
import com.satyabhushan.journalapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck(){
        log.info("Checking System Health");
        log.info("Health Check is OK");
        return new ResponseEntity<>("OK" , HttpStatus.OK);
    }

    @Autowired
    private UserService userService ;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) throws UserAlreadyExists {
        userService.checkUserWithUserName(user.getUserName());
        boolean userResponse = userService.saveUserEntryByEncryptedPassword(user);
        return new ResponseEntity<>(userResponse , HttpStatus.CREATED);
    }
}
