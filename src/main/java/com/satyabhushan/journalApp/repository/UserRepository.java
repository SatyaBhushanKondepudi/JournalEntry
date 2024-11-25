package com.satyabhushan.journalApp.repository;

import com.satyabhushan.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUserName(String username);

    void deleteByUserName(String userName);
}
