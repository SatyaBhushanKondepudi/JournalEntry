package com.satyabhushan.journalApp.service;

import com.satyabhushan.journalApp.entity.JournalEntry;
import com.satyabhushan.journalApp.entity.User;
import com.satyabhushan.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
            Optional<User> findUserByUserName = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);
            journalEntryResponse = savedJournalEntry;
            findUserByUserName.ifPresent(user -> {
                user.getJournalEntries().add(savedJournalEntry);
//                user.setUserName(null);
                userService.saveEntry(findUserByUserName.get());
            });
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
    public boolean deleteById(ObjectId id , String userName) {
        AtomicBoolean ans = new AtomicBoolean(false);
        try{
            Optional<User> findUserByUserName = userService.findByUserName(userName);
            findUserByUserName
                    .ifPresent(user ->
                    {
                        List<JournalEntry> journalEntriesWithGivenId = user.getJournalEntries()
                                .stream()
                                .filter(journalEntry -> journalEntry.getId().equals(id))
                                .collect(Collectors.toList());
                        if(!journalEntriesWithGivenId.isEmpty()){
                            user.getJournalEntries()
                                .removeIf(jounalEntry -> jounalEntry.getId().equals(id));
                            ans.set(true);
                        }
                        userService.saveEntry(findUserByUserName.get());
                    });
            journalEntryRepository.deleteById(id);
//            ans.set(true);
        }catch(Exception E){
            log.error(E.getMessage());
            throw new RuntimeException("An Error occured while deleting the entry.." , E);
        }
        return ans.get();

    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }


}