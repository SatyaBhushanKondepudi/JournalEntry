package com.satyabhushan.journalapp.service;

import com.satyabhushan.journalapp.entity.User;
import com.satyabhushan.journalapp.exceptions.UserAlreadyExists;
import com.satyabhushan.journalapp.exceptions.UserNotFoundWithGivenUserName;
import com.satyabhushan.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User saveEntry(User user){
        return userRepository.save(user);
    }

    public void checkUserWithUserName(String userName) throws UserAlreadyExists{
        Optional<User> byUserName = userRepository.findByUserName(userName);
        if(byUserName.isPresent()){
            throw new UserAlreadyExists("User Already Exists with given UserName : " + userName);
        }
    }

    public boolean saveUserEntryByEncryptedPassword(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            log.info("User Entry : " + user);
            userRepository.save(user);
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
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

    public User findByUserName(String userName) throws UserNotFoundWithGivenUserName {
        Optional<User> findbyUserName = userRepository.findByUserName(userName);
        if(findbyUserName.isPresent()){
            return findbyUserName.get();
        }
        else{
            throw new UserNotFoundWithGivenUserName("User not found with given userName");
        }
    }

    public void deleteUser(ObjectId id) {
       userRepository.deleteById(id);
    }

    public void deleteUserByUserName(String userName){
        userRepository.deleteByUserName(userName);
    }
}
