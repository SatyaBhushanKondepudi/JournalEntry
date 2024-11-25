package com.satyabhushan.journalApp.repository;

import com.satyabhushan.journalApp.entity.JournalEntry;
import com.satyabhushan.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry , ObjectId> {
}
