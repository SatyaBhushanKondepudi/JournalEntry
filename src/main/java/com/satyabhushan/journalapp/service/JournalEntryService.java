package com.satyabhushan.journalapp.service;

import com.satyabhushan.journalapp.entity.JournalEntry;
import com.satyabhushan.journalapp.entity.User;
import com.satyabhushan.journalapp.exceptions.JournalEntryNotFoundException;
import com.satyabhushan.journalapp.exceptions.UserNotFoundWithGivenUserName;
import com.satyabhushan.journalapp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String userName) {
        JournalEntry journalEntryResponse = null;
        try{
            User findUserByUserName = userService.findByUserName(userName);
            if(findUserByUserName != null){
                journalEntry.setDate(LocalDateTime.now());
                JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);
                findUserByUserName.getJournalEntries().add(savedJournalEntry);
                userService.saveEntry(findUserByUserName);
            }
            else{
                log.error("User with provided UserName : " + userName + "not found");
                throw new UserNotFoundWithGivenUserName("User with provided UserName : " + userName + "not found");
            }
        } catch (Exception e) {
            log.error("e: ", e);
            throw new RuntimeException("An Error Occured while saving the Entry..");
        }
        return journalEntryResponse;
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id , String userName) throws JournalEntryNotFoundException, UserNotFoundWithGivenUserName {

        User findUserByUserName = userService.findByUserName(userName);
        boolean existsById = journalEntryRepository.existsById(id);
        if(existsById){
            log.info("Journal Entry found with given id : " + id);
            findUserByUserName
                    .getJournalEntries()
                    .removeIf(journalEntry -> journalEntry.getId().equals(id));
            userService.saveEntry(findUserByUserName);
            journalEntryRepository.deleteById(id);
            return true;
        }
        else{
            log.error("Journal Entry not found with given id : " + id);
            throw new JournalEntryNotFoundException("Journal Entry not found with given id : " + id);
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        if(journalEntry == null){
            throw new RuntimeException("Journal Entry is null");
        }
        journalEntryRepository.save(journalEntry);
    }


}