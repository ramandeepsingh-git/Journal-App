package net.engineeringdigest.journalApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.dto.UserDto;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin API's", description = "Get, Update and Delete User")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AppCache appCache;
    @GetMapping("/all-users")
    @Operation(summary = "Get all Users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getall();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/create-admin-user")
    @Operation(summary = "Create new Admin")
    public void createUser(@RequestBody UserDto user){
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setSentimentAnalysis(user.isSentimentAnalysis());
        userService.saveAdmin(newUser);
    }
    @GetMapping("clear-app-cache")
    @Operation(summary = "clear app cache")
    public void clearAppCache(){
        appCache.init();
    }
}
