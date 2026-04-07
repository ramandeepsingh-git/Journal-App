package net.engineeringdigest.journalApp.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSchedulerTest {

    @Autowired
    private UserScheduler userScheduler;
    @Test
    public void testFetchUserAndSendEmail(){
        userScheduler.fetchUsersAndSendSaMail();
    }
}
