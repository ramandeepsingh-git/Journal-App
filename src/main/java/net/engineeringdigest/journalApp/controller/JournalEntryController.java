package net.engineeringdigest.journalApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineeringdigest.journalApp.dto.JournalEntryDto;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal API's", description = "Create, Get all, Get by id, Delete by id and Update Journal Entries")
public class JournalEntryController {
    @Autowired
    public JournalEntryService journalEntryService;

    @Autowired
    public UserService userService;

    @GetMapping
    @Operation(summary = "Get all journal entries of User")
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> allentries = user.getJournalentries();
        if(allentries != null && !allentries.isEmpty()){
            return new ResponseEntity<>(allentries,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping
    @Operation(summary = "Post a Journal Entry of User")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntryDto myEntry){
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId(myEntry.getId());
        journalEntry.setContent(myEntry.getContent());
        journalEntry.setTitle(myEntry.getTitle());
        journalEntry.setDate(myEntry.getDate());
        journalEntry.setSentiment(myEntry.getSentiment());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.saveEntry(journalEntry,username);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("id/{myid}")
    @Operation(summary = "Get a Journal Entry of User by id")
    public ResponseEntity<JournalEntry> getJournalEntrybyid(@PathVariable String myid){
        ObjectId id = new ObjectId(myid);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getJournalentries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalentry = journalEntryService.findbyid(id);
            if (journalentry.isPresent()) {
                return new ResponseEntity<>(journalentry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("id/{myid}")
    @Operation(summary = "Delete a Journal Entry of User by id")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteById(myid, username);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("id/{myid}")
    @Operation(summary = "Update a Journal Entry of User")
    public ResponseEntity<?> updateEntry(@PathVariable String myid,@RequestBody JournalEntryDto newEntry){
        ObjectId id = new ObjectId(myid);
        JournalEntry newjournalEntry = new JournalEntry();
        newjournalEntry.setId(newEntry.getId());
        newjournalEntry.setContent(newEntry.getContent());
        newjournalEntry.setTitle(newEntry.getTitle());
        newjournalEntry.setDate(newEntry.getDate());
        newjournalEntry.setSentiment(newEntry.getSentiment());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getJournalentries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());

        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalentry = journalEntryService.findbyid(id);
            if (journalentry.isPresent()) {
                JournalEntry old = journalentry.get();
                old.setTitle(newjournalEntry.getTitle() != null && !newjournalEntry.getTitle().equals("") ? newjournalEntry.getTitle() : old.getTitle());
                old.setContent(newjournalEntry.getContent() != null && !newjournalEntry.getContent().equals("") ? newjournalEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}