package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.services.UserDetailServiceimpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    @InjectMocks
    public UserDetailServiceimpl userDetailsService;

    @Mock
    public UserRepository userRepository;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void loadUserByUsernameTest(){
        when (userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(User.builder(). username("ram") . password("inrinrick") .roles(new ArrayList<>()).build());
        UserDetails user = userDetailsService. loadUserByUsername("ram");
        Assertions.assertNotNull(user);
    }

}
