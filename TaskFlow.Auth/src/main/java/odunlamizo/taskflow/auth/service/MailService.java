package odunlamizo.taskflow.auth.service;

import jakarta.mail.MessagingException;

public interface MailService {

    void send(String to, String from, String subject, String text) throws MessagingException;
    
}