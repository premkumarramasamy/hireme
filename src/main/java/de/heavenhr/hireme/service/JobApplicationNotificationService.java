package de.heavenhr.hireme.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobApplicationNotificationService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationNotificationService.class);
    
    public void doStatusChangeNotifications() {
        /* for now I have just added this method which prints the status change
         * To be in a cleaner way we need to have a listener, listens for status change events 
         * does initiates the notification for the candidate
         */
        
        LOGGER.info("Would like send notification to candidate email");
    }
}
