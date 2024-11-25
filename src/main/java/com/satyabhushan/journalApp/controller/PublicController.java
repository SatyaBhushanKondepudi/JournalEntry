package com.satyabhushan.journalApp.controller;

import com.satyabhushan.journalApp.entity.User;
import com.satyabhushan.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

    @Autowired
    private UserService userService ;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User userResponse = userService.saveUserEntryByEncryptedPassword(user);
        return new ResponseEntity<>(userResponse , HttpStatus.CREATED);
    }
}
