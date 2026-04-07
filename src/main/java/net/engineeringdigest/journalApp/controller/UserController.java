package net.engineeringdigest.journalApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.dto.UserDto;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserService;
import net.engineeringdigest.journalApp.services.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "User API's", description = "Get, Update and Delete User")
public class  UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WeatherService weatherService;

    @PutMapping
    @Operation(summary = "Update Current User")
    public ResponseEntity<?> updateUser (@RequestBody UserDto user){
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setSentimentAnalysis(user.isSentimentAnalysis());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userinDB = userService.findByUsername(username);
        userinDB.setUsername(newUser.getUsername());
        userinDB.setPassword(newUser.getPassword());
        userService.saveNewUser(userinDB);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Operation(summary = "Delete User")
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Greet User (Weather Api)")
    public ResponseEntity<?> greetings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Munich");
        String greetings = "";
        if (weatherResponse!= null){
            greetings = "\nWeather : " + weatherResponse.getCurrent().getTemperature()
                    + "\nHumidity: " + weatherResponse.getCurrent().getHumidity()
                    + "\nWind Speed: " + weatherResponse.getCurrent().getWindSpeed()
                    + "\nFeels Like: " + weatherResponse.getCurrent().getFeelslike()
                    + "\nVisibility: " + weatherResponse.getCurrent().getVisibility()
                    + "\nDescription: " + weatherResponse.getCurrent().getWeatherDescriptions();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greetings, HttpStatus.OK);
    }
}
