package com.satyabhushan.journalApp.controller;

import com.satyabhushan.journalApp.entity.JournalEntry;
import com.satyabhushan.journalApp.entity.User;
import com.satyabhushan.journalApp.service.JournalEntryService;
import com.satyabhushan.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService ;

    @Autowired
    private UserService userService;

//    @GetMapping
//    public ResponseEntity<List<JournalEntry>> getAllJournalEntries(){
//        List<JournalEntry> allJournalEntries = journalEntryService.getAll();
//        return new ResponseEntity<>(allJournalEntries , HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> user = userService.findByUserName(userName);
        if(user.isPresent()){
            List<JournalEntry> journalEntriesList = user.get().getJournalEntries();
            if(journalEntriesList != null && !journalEntriesList.isEmpty()){
                return new ResponseEntity<>(journalEntriesList , OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntryForParticularUserName(@RequestBody JournalEntry myEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            JournalEntry response = journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(response , HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myID}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> user = userService.findByUserName(userName);
        List<JournalEntry> findTheMatchingJournalEntryWithGivenID = user.get().getJournalEntries()
                .stream()
                .filter(journalEntry -> (journalEntry.getId().equals(myID)))
                .collect(Collectors.toList());
        if(!findTheMatchingJournalEntryWithGivenID.isEmpty()){
            Optional<JournalEntry> journalEntryById = journalEntryService.findById(myID);
            return new ResponseEntity<>(journalEntryById.get() , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myID}")
    public ResponseEntity<?> deleteJournalEntryByIdInBothJounalEntryAndUserTable( @PathVariable ObjectId myID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean response = journalEntryService.deleteById(myID, userName);
        if(response == true){
            return new ResponseEntity<>(response , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{myID}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable ObjectId myID,
                                                               @RequestBody JournalEntry newEntry
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> user = userService.findByUserName(userName);
        List<JournalEntry> findTheMatchingJournalEntryWithGivenID = user.get().getJournalEntries()
                .stream()
                .filter(journalEntry -> (journalEntry.getId().equals(myID)))
                .collect(Collectors.toList());
        if(!findTheMatchingJournalEntryWithGivenID.isEmpty()){
            Optional<JournalEntry> journalEntryById = journalEntryService.findById(myID);
            if(journalEntryById.isPresent()){
                JournalEntry oldJournalEntry = journalEntryById.get();
                oldJournalEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldJournalEntry.getTitle());
                oldJournalEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldJournalEntry.getContent());
                journalEntryService.saveEntry(oldJournalEntry);
                return new ResponseEntity<>(oldJournalEntry , OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
