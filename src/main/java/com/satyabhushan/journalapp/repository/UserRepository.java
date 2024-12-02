package com.satyabhushan.journalapp.repository;

import com.satyabhushan.journalapp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

//@Component
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUserName(String username);

    void deleteByUserName(String userName);
}
