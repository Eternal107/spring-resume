package net.study.resume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationManagerService {

    @Autowired
    private JavaMailSender mailSender;

   public void sendRestoreLink(String token,String profileEmail){
       final String subject = "Restore password";
       final String confirmationUrl =  "http://localhost:8080/restore/" + token;
       final SimpleMailMessage email = new SimpleMailMessage();
       email.setTo(profileEmail);
       email.setSubject(subject);
       email.setText("To restore access please click on the link."+ confirmationUrl);

       email.setFrom("resume@gmail.com");
       mailSender.send(email);
   }

    public void sendMailConfirmation(String token,String profileEmail){
        final String subject = "Registration Confirmation";
        final String confirmationUrl =  "http://localhost:8080/sign-up/" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(profileEmail);
        email.setSubject(subject);
        email.setText("To confirm email please click on the link."+ confirmationUrl);

        email.setFrom("resume@gmail.com");
        mailSender.send(email);
    }
}
