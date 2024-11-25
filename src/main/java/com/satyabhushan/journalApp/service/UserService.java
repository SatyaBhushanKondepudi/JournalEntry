package com.satyabhushan.journalApp.service;

import com.satyabhushan.journalApp.entity.JournalEntry;
import com.satyabhushan.journalApp.entity.User;
import com.satyabhushan.journalApp.repository.JournalEntryRepository;
import com.satyabhushan.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository ;

    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User saveEntry(User user){
        return userRepository.save(user);
    }

    public User saveUserEntryByEncryptedPassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        return userRepository.save(user);
    }

    public User saveAdminUserEntryByEncryptedPassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER" , "ADMIN"));
        return userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }

    public Optional<User> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public void deleteUser(ObjectId id) {
       userRepository.deleteById(id);
    }

    public void deleteUserByUserName(String userName){
        userRepository.deleteByUserName(userName);
    }
}
